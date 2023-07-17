package com.example.annotations

@Target(AnnotationTarget.FUNCTION)
annotation class ChaiRoute(val routeName: String ="") {
}