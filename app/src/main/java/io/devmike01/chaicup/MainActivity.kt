package io.devmike01.chaicup

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.devs.vectorchildfinder.VectorChildFinder
import com.devs.vectorchildfinder.VectorDrawableCompat
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
        VectorFinder()
        Greeting02(name = name)
    }
}

@Composable
fun VectorFinder(){
    AndroidView(factory = {context ->
        val iv = ImageView(context)
        val vector = VectorChildFinder(context,
            io.devmike01.chaicup.sample.R.drawable.baseline_add_chart_24,
            iv)
        val path1 : VectorDrawableCompat.VFullPath = vector.findPathByName("mypath1")
        path1.fillColor = Color.YELLOW
        iv
    }, update = {
        it.invalidate()
    })
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
