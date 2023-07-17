package dev.gbenga.compiler

import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSPropertyDeclaration

interface KSVisitor<D, R> {
    fun visitNode(node: KSNode, data: D) : R

    fun visitClassDeclaration(functionDeclaration: KSFunctionDeclaration, data: D): R

    fun visitPropertyDeclaration(property : KSPropertyDeclaration, data: D): R

}