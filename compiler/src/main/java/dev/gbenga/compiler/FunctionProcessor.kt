package dev.gbenga.compiler

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFunction
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.validate
import java.io.OutputStream

class FunctionProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val option: Map<String, String>
) : SymbolProcessor{
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver
            .getSymbolsWithAnnotation("com.example.annotations.ChaiRoute")
            .filterIsInstance<KSFunctionDeclaration>()
        if (!symbols.iterator().hasNext())return emptyList()

        var file : OutputStream  = codeGenerator.createNewFile(
            dependencies = Dependencies(false,
                *resolver.getAllFiles().toList().toTypedArray()),
            packageName = "dev.gbenga",
            fileName = "GeneratedFunctions"
        )

        file += "package dev.gbenga\n"

        symbols.forEach {ksFunc ->
            ksFunc.accept(Visitor(file), Unit)
        }

        file.close()
        return symbols.filterNot{
            it.validate()
        }.toList()
    }

    operator fun OutputStream.plusAssign(str: String){
        this.write(str.toByteArray())
    }



}