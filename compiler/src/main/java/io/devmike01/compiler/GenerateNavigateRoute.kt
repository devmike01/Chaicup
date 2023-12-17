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
import io.devmike01.compiler.android.Imports
import java.io.OutputStream
import java.util.regex.Pattern

class GenerateNavigateRoute(private val codeGenerator: CodeGenerator,
                            private val logger: KSPLogger,) : CodeCompiler {

    private lateinit var navigatorFile : OutputStream

    override fun process(
        resolver: Resolver,
        visitor: (OutputStream) -> KSVisitorVoid
    ): List<KSAnnotated> {
        val symbols = resolver
            .getSymbolsWithAnnotation("io.devmike01.annotations.ChaiNavigation")
            .filterIsInstance<KSFunctionDeclaration>()
        if (!symbols.iterator().hasNext())return emptyList()


        generateNavHost(resolver, symbols)

        symbols.forEach {ksFunc ->
            ksFunc.accept(visitor(navigatorFile), Unit)
        }

        ChaiCupGenerator.generatePrivateFunc {
            navigatorFile += it
        }

        navigatorFile += "\n\n}"
        navigatorFile.close()
        return symbols.filterNot{
            it.validate()
        }.toList()
    }

    private fun generateNavHost(resolver: Resolver, symbols: Sequence<KSFunctionDeclaration>){
        // Generate nav host class
        navigatorFile = codeGenerator.createNewFile(
            dependencies = Dependencies(false,
                *resolver.getAllFiles().toList().toTypedArray()),
            packageName = "io.devmike01",
            fileName = FileName.CHAICUP_NAVHOST_FILENAME
        )

        navigatorFile += FileName.CHAICUP_PKG_NAME

        Imports.navigation { import ->
            navigatorFile += import
        }

        navigatorFile += "\nobject ChaiCupNavRoutes{\n\n "
    }


    override fun visitFunctionDeclaration(pkgName: String, simpleName: String, annotations: Map<String, KSAnnotation>) {

        val annotation: KSAnnotation = annotations["ChaiNavigation"] ?: return

        val nameArg: KSValueArgument = annotation.arguments.first { argument ->
            argument.name?.asString() == "routeName"
        }

       // val functionNames = nameArg.value as String

        // Generate the route
        val route = ChaiCupGenerator.computeRouteName(nameArg.value as String, simpleName)

        val sanitisedRoute = if (route.contains("?")){
            route.split("?")[0]
        }else{
            route
        }
        // For chaicup routes
        ChaiCupGenerator.navigator(simpleName, route.contains("?"), sanitisedRoute){code ->
            navigatorFile += "   const val $sanitisedRoute  = \"/$pkgName.${nameArg.value}\"\n"
            navigatorFile += code
        }


    }

    override fun visitPropertyDeclaration(property: KSPropertyDeclaration) {
        // Generating argument name.
        val argumentName = property.simpleName.asString()
        navigatorFile += "    $argumentName: "

        // Generating argument type.
        val resolvedType: KSType = property.type.resolve()
        navigatorFile += resolvedType.declaration.qualifiedName?.asString() ?: run {
            logger.error("Invalid property type", property)
            return
        }

        // Handling nullability.
        navigatorFile += if (resolvedType.nullability == Nullability.NULLABLE) "?" else ""

        navigatorFile += ",\n"
    }
}