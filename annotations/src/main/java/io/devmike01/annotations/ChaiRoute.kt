package io.devmike01.annotations

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ChaiRoute(val routeName: String ="") {
}