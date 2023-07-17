package dev.gbenga.compiler

import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSTypeArgument
import com.google.devtools.ksp.symbol.KSVisitorVoid
import java.io.OutputStream

class Visitor(file: OutputStream) : KSVisitorVoid() {

    override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: Unit) {
        super.visitFunctionDeclaration(function, data)
    }

    override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: Unit) {
        super.visitPropertyDeclaration(property, data)
    }

    override fun visitTypeArgument(typeArgument: KSTypeArgument, data: Unit) {
        super.visitTypeArgument(typeArgument, data)
    }
}