package io.devmike01.chaicup

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.devs.vectorchildfinder.VectorChildFinder
import com.devs.vectorchildfinder.VectorDrawableCompat
import io.devmike01.annotations.ChaiNavigation
import io.devmike01.annotations.ChaiRoute
import io.devmike01.chaicup.ChaiCupNavRoutes.navigateToGreeting02
import io.devmike01.chaicup.ChaiCupNavRoutes.navigateToGreetingWithNoArgument
import io.devmike01.chaicup.ui.theme.ChaiCupTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import rememberCollapsibleHeaderScaffoldState

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ChaiCupTheme {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()
                NavHost(navController = navController,
                    startDestination = ChaiCupRoutes.GREETING_ROUTE){
                    composable(ChaiCupNavRoutes.FOOD_PAGE,
                        arguments = listOf(navArgument("arg0"){
                            defaultValue ="None"
                        }, navArgument("myArg"){
                            defaultValue ="None"
                        })){
                        Greeting02(navController, name = "Hello : ${it.arguments?.getString("arg0")} \n${it.arguments?.getString("myArg")}")
                    }
                    composable(ChaiCupRoutes.GREETING_ROUTE){
                        Greeting(navController,
                            name = "Route: ${ChaiCupRoutes.GREETING_ROUTE}")
                    }

                    composable(ChaiCupNavRoutes.FOOD_PAGE_ROUTE){
                        GreetingWithNoArgument(name = "NONE")
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
            navController.navigateToGreeting02(arguments = arrayOf("Hello World", "Hello World x 2", "what"))

        }) {

            Text(text = "Navigate to Greeting 2")
        }
        VectorFinder()
        Greeting02(navController, name = name)
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


@ChaiNavigation(routeName = "FoodPage?myArg={myArg}&arg0={arg0}&arg1={arg1}")
@Composable
fun Greeting02(nav: NavController,name: String) {

    Button(onClick = {
        nav.navigateToGreetingWithNoArgument()
    }) {

        Text(text = "ARGUMENT: $name")
    }


}

@ChaiNavigation(routeName = "FoodPage")
@Composable
fun GreetingWithNoArgument(name: String) {

    Button(onClick = {
    }) {

        Text(text = "GreetingWithNoArgument: $name")
    }


}
