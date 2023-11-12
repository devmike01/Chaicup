package io.devmike01.compiler

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSValueArgument
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.symbol.Nullability
import com.google.devtools.ksp.validate
import io.devmike01.compiler.android.ChaiCupGenerator
import java.io.OutputStream
import java.util.regex.Pattern

class GenerateChaiRoute(private val codeGenerator: CodeGenerator,
                        private val logger: KSPLogger,) : CodeCompiler {
    private lateinit var file: OutputStream
    val routes = mutableListOf<String>()

    override fun process(resolver: Resolver,
                         visitor: (OutputStream) -> KSVisitorVoid): List<KSAnnotated> {
        val symbols = resolver
            .getSymbolsWithAnnotation("io.devmike01.annotations.ChaiRoute")
            .filterIsInstance<KSFunctionDeclaration>()
        if (!symbols.iterator().hasNext())return emptyList()

        file  = codeGenerator.createNewFile(
            dependencies = Dependencies(false,
                *resolver.getAllFiles().toList().toTypedArray()),
            packageName = "io.devmike01",
            fileName = FileName.CHAICUP_ROUTE
        )

        file += "package io.devmike01.chaicup\n"

        file  +="object ${FileName.CHAICUP_ROUTE}{"

        file += "\n    val routes = mutableMapOf<String, String>()\n"

        symbols.forEach {ksFunc ->
            ksFunc.accept(visitor(file), Unit)
        }


        // Add all routes to a list
        file += "\n\n   fun getChaiCupRoutes(): Map<String, String>{\n"
               routes.forEach {
                   file +=  "        routes[$it] = $it\n"
               }
        file += "        return routes\n"
                file +=       "    }"
        file += "\n}"

        file.close()
        return symbols.filterNot{
            it.validate()
        }.toList()
    }


    override fun visitFunctionDeclaration(pkgName: String, simpleName: String,
                                          annotations: Map<String, KSAnnotation>) {
        try {

            val annotation : KSAnnotation = annotations["ChaiRoute"] ?: return

            val navAnnotation : KSAnnotation? = annotations["ChaiNavigation"]


            val nameArg : KSValueArgument = annotation.arguments.first {
                    argument ->  argument.name?.asString() =="routeName"
            }

            val navRouteArg: KSValueArgument? = navAnnotation?.arguments?.first {
                    argument ->  argument.name?.asString() =="routeName"
            }

            val route = ChaiCupGenerator.computeRouteName(nameArg.value as String? ?: "", simpleName)

            val navRoute = ChaiCupGenerator.computeRouteName(navRouteArg?.value as String? ?: "", simpleName)


            if (!routes.contains(route)){
                routes.add(route)

                // For chaicup routes
                file += "\n    const val $route"
                file += " = \"/$pkgName.$simpleName\""
            }

            if ((!routes.contains(navRoute)).and(navRoute.isNotBlank())){
                routes.add(navRoute)

                // For chaicup routes
                file += "\n    const val $navRoute"
                file += " = \"/$pkgName.$simpleName\""

            }


        }catch (e: NoSuchElementException){
            logger.info("Skipping annotation")
        }
    }

    override fun visitPropertyDeclaration(property: KSPropertyDeclaration) {
        // Generating argument name.
        val argumentName = property.simpleName.asString()
        file += "    $argumentName: "

        // Generating argument type.
        val resolvedType: KSType = property.type.resolve()
        file += resolvedType.declaration.qualifiedName?.asString() ?: run {
            logger.error("Invalid property type", property)
            return
        }

        // Handling nullability.
        file += if (resolvedType.nullability == Nullability.NULLABLE) "?" else ""

        file += ",\n"
    }
}