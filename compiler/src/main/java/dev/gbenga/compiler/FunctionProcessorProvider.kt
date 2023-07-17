package dev.gbenga.compiler

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import dev.gbenga.compiler.FunctionProcessor

class FunctionProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return FunctionProcessor(
            codeGenerator = environment.codeGenerator,
            logger = environment.logger,
            option = environment.options
        )
    }
}