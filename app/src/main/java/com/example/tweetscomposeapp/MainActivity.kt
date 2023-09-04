package com.example.tweetscomposeapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tweetscomposeapp.api.TweetsyAPI
import com.example.tweetscomposeapp.screens.CategoryScreen
import com.example.tweetscomposeapp.screens.DetailScreen
import com.example.tweetscomposeapp.ui.theme.TweetsComposeAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var tweetsyAPI: TweetsyAPI
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GlobalScope.launch {
            val response = tweetsyAPI.getCategories()
            Log.d("MainActivity", response.body()!!.distinct().toString())
        }
        setContent {
            TweetsComposeAppTheme {
                Scaffold(topBar = {
                    TopAppBar(title = {
                        Text(text = "Tweetsy")
                    }, backgroundColor = Color.Black, contentColor = Color.White)
                }) {
                    Box(modifier = Modifier.padding(it)) {
                        App()
                    }
                }

            }

        }
    }
}

@Composable
fun App() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "category") {
        composable(route = "category") {
            CategoryScreen {
                navController.navigate("detail/${it}")
            }
        }
        composable(route = "detail/{category}",
            arguments = listOf(
                navArgument("category") {
                    type = NavType.StringType
                }
            )
        ) {
            DetailScreen()
        }
    }

}