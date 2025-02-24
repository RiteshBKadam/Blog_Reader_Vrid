package com.example.blogreadervrid

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "blogList") {
                composable("blogList") {
                    BlogListScreen(navController)
                }
                composable("webview/{url}") { backStackEntry ->
                    val url = backStackEntry.arguments?.getString("url") ?: ""
                    BlogWebViewScreen(url)
                }
            }
        }
    }
}

@Composable
fun BlogListScreen(navController: NavController) {
    val viewModel=BlogViewModel()
    BlogListScreen(viewModel) { url ->
        navController.navigate("webview/${Uri.encode(url)}")
    }
}
