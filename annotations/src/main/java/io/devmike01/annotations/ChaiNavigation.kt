package io.devmike01.annotations

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ChaiNavigation(val routeName: String ="") {
}