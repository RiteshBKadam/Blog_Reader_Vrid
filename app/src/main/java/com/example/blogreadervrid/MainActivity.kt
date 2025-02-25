package com.example.blogreadervrid

import BlogRepository
import BlogViewModelFactory
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.example.blogreadervrid.room.BlogDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = BlogDatabase.getDatabase(this)
        val repository = BlogRepository(RetrofitInstance.api, database.blogPostDao())

        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "blogList") {
                composable("blogList") {
                    BlogListScreen(navController, repository)  // Pass repository
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
fun BlogListScreen(navController: NavController, repository: BlogRepository) {
    val viewModel: BlogViewModel = viewModel(factory = BlogViewModelFactory(repository))
    BlogListScreen(viewModel) { url ->
        navController.navigate("webview/${Uri.encode(url)}")
    }
}
