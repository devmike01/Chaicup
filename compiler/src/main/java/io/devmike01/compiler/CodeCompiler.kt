package io.devmike01.compiler

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSVisitor
import com.google.devtools.ksp.symbol.KSVisitorVoid
import java.io.OutputStream

interface CodeCompiler {
    fun process(resolver: Resolver, visitor: (OutputStream) -> KSVisitorVoid) : List<KSAnnotated>
    fun visitFunctionDeclaration(pkgName: String, simpleName: String, annotations: Map<String, KSAnnotation>)
    fun visitPropertyDeclaration(property: KSPropertyDeclaration)
}