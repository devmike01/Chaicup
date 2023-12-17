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
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.symbol.Nullability
import com.google.devtools.ksp.validate
import java.io.OutputStream
import java.util.regex.Pattern

class FunctionProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val option: Map<String, String>
) : SymbolProcessor{

    private val injector : InjectorImpl = InjectorImpl(codeGenerator, logger)

    operator fun OutputStream.plusAssign(str: String){
        this.write(str.toByteArray())
    }

    inner class Visitor(private var file: OutputStream) : KSVisitorVoid() {

        override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: Unit) {
            injector.visitAllFunctionDeclaration(function)
        }

        override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: Unit) {
            injector.visitAllPropertyDeclaration(property)
        }

        override fun visitTypeArgument(typeArgument: KSTypeArgument, data: Unit) {
            super.visitTypeArgument(typeArgument, data)
        }
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        return injector.processAll(resolver){
            Visitor(it)
        }
    }


}
