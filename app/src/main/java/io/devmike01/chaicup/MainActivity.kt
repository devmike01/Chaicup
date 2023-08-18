package io.devmike01.chaicup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.devmike01.annotations.ChaiRoute
import io.devmike01.chaicup.ui.theme.ChaiCupTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            ChaiCupTheme {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()
                NavHost(navController = navController,
                    startDestination = ChaiCupRoutes.GREETING_ROUTE){
                    composable(ChaiCupRoutes.GREETING02_ROUTE){
                        Greeting02(name = "Hello : ${ChaiCupRoutes.GREETING02_ROUTE}")
                    }
                    composable(ChaiCupRoutes.GREETING_ROUTE){
                        Greeting(navController,
                            name = "Route: ${ChaiCupRoutes.GREETING_ROUTE}")
                    }
                }
            }
        }
    }

}

@Composable
@ChaiRoute
fun Greeting(navController: NavController, name: String) {
    Column {
        Button(onClick = {
            navController.navigate(ChaiCupRoutes.GREETING02_ROUTE)
        }) {
            Text(text = "Navigate to Greeting 2")
        }
        Greeting02(name = name)
    }
}


@Composable
@ChaiRoute
fun Greeting02(name: String) {
    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}
