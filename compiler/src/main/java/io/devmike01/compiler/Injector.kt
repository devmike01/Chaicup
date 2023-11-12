package io.devmike01.compiler

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import org.koin.core.component.getScopeName
import java.io.OutputStream

interface Injector{
    fun processAll(resolver: Resolver, visitor: (OutputStream) -> KSVisitorVoid) : List<KSAnnotated>
    fun visitAllFunctionDeclaration(function: KSFunctionDeclaration)
    fun visitAllPropertyDeclaration(property: KSPropertyDeclaration)
}

class InjectorImpl(
    codeGenerator: CodeGenerator,
    logger: KSPLogger) : Injector{
    private val generators = mutableListOf<CodeCompiler>()


    init {
        generators.add(GenerateChaiRoute(codeGenerator, logger))
        generators.add(GenerateNavigateRoute(codeGenerator, logger))
    }

    override fun processAll(
        resolver: Resolver,
        visitor: (OutputStream) -> KSVisitorVoid
    ): List<KSAnnotated> {

        val ksAnnotations = mutableListOf<KSAnnotated>()
        generators.forEach {
            it.process(resolver, visitor).forEach {ksAnnotated ->
                ksAnnotations.add(ksAnnotated)
            }
        }
        return ksAnnotations
    }

    override fun visitAllFunctionDeclaration(function: KSFunctionDeclaration) {
        generators.forEach {
            val annotations = function.annotations.associateBy {kAnnot ->
                kAnnot.shortName.asString()
            }


            it.visitFunctionDeclaration(function.packageName.asString(),
                function.simpleName.asString(),
                annotations)
        }
    }

    override fun visitAllPropertyDeclaration(property: KSPropertyDeclaration) {
        generators.forEach {
            it.visitPropertyDeclaration(property)
        }
    }

}