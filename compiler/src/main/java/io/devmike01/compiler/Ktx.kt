package io.devmike01.compiler

import java.io.OutputStream


operator fun OutputStream.plusAssign(str: String){
    this.write(str.toByteArray())
}
