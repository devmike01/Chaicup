package io.devmike01.compiler.android

object Imports {

    const val IMPORT ="import"

    fun navCompose(runImports: (String) -> Unit){
        listOf("rememberNavController",
            "composable", "NavHost",
            "NavHost", "NavHostController", "",).forEach {
            runImports.invoke("$IMPORT androidx.navigation.compose.$it\n")
        }
    }

    fun runtime(runImports: (String) -> Unit){
        listOf<String>(
            "Composable"
        ).forEach {
            runImports("$IMPORT androidx.compose.runtime.$it\n")
        }
    }

    fun composeUi(runImports: (String) -> Unit){
        listOf<String>("Modifier", "Alignment").forEach {
            runImports("$IMPORT androidx.compose.ui.$it\n")
        }
    }

    fun composeAnimation(runImports: (String) -> Unit){
        listOf<String>("AnimatedContentTransitionScope", "Alignment").forEach {
            runImports("$IMPORT androidx.compose.animation.$it\n")
        }
    }

    fun navigation(runImports: (String) -> Unit){
        listOf<String>("NavController", "NavGraphBuilder", "NavOptions", "Navigator").forEach {
            runImports("$IMPORT androidx.navigation.$it\n")
        }
    }

    fun android(runImports: (String) -> Unit){
        listOf<String>("Log").forEach {
            runImports("$IMPORT android.util.$it\n")
        }
    }
}