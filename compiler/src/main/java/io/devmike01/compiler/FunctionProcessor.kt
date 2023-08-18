package io.devmike01.compiler

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeArgument
import com.google.devtools.ksp.symbol.KSValueArgument
import com.google.devtools.ksp.symbol.KSValueParameter
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.symbol.Nullability
import com.google.devtools.ksp.validate
import java.io.OutputStream

class FunctionProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val option: Map<String, String>
) : SymbolProcessor{
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver
            .getSymbolsWithAnnotation("io.devmike01.annotations.ChaiRoute")
            .filterIsInstance<KSFunctionDeclaration>()
        if (!symbols.iterator().hasNext())return emptyList()

        val file : OutputStream  = codeGenerator.createNewFile(
            dependencies = Dependencies(false,
                *resolver.getAllFiles().toList().toTypedArray()),
            packageName = "io.devmike01",
            fileName = "GeneratedRoutes"
        )

        file += "package io.devmike01.chaicup\n"

        file  +="object ChaiCupRoutes{"

        symbols.forEach {ksFunc ->
            ksFunc.accept(Visitor(file), Unit)
        }
        file += "\n}"

        file.close()
        return symbols.filterNot{
            it.validate()
        }.toList()
    }

    operator fun OutputStream.plusAssign(str: String){
        this.write(str.toByteArray())
    }


    inner class Visitor(private var file: OutputStream) : KSVisitorVoid() {

        override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: Unit) {
            val annotation : KSAnnotation = function.annotations.first{
                it.shortName.asString() == "ChaiRoute"
            }

            val nameArg : KSValueArgument = annotation.arguments.first {
                    argument ->  argument.name?.asString() =="routeName"
            }

            val functionName = nameArg.value as String


            function.simpleName.getQualifier()

            val pckName = function.packageName.asString()
            val funcName = function.simpleName.asString()
            file += "\n    const val ${(if(functionName.isBlank()) funcName else functionName).uppercase()}_ROUTE"
            file += " = \"/$pckName.$funcName\""

        }

        override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: Unit) {
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

        override fun visitTypeArgument(typeArgument: KSTypeArgument, data: Unit) {
            super.visitTypeArgument(typeArgument, data)
        }
    }


}
