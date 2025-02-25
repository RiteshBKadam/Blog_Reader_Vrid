package com.example.blogreadervrid

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BlogListScreen(viewModel: BlogViewModel, onBlogClick: (String) -> Unit) {
    val blogs by viewModel.blogs.collectAsState()
    val listState = rememberLazyListState()
    val context = LocalContext.current
    var shouldRefresh by remember { mutableStateOf(false) }
    var isInternetAvailable by remember { mutableStateOf(NetworkUtils.isInternetAvailable(context)) }
    var buttonText by remember { mutableStateOf("Turn on Internet") }

    LaunchedEffect(shouldRefresh) {
        if (shouldRefresh) {
            isInternetAvailable = NetworkUtils.isInternetAvailable(context)
            shouldRefresh = false
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .collect { layoutInfo ->
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                if (lastVisibleItemIndex >= blogs.size - 2 && !viewModel.isFetching) {
                    viewModel.fetchBlogs()
                }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Top Blogs", color = Color.White) },
                colors = TopAppBarColors(
                    containerColor = Color(0xA61C1C1C),
                    titleContentColor = Color.White,
                    scrolledContainerColor = Color.Black,
                    actionIconContentColor = Color.Black,
                    navigationIconContentColor = Color.Black
                )
            )
        },
        containerColor = Color.Black
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(color = Color.Black)
        ) {
            if (!isInternetAvailable) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Need Internet Connection!", color = Color.White, fontSize = 17.sp)
                    Spacer(modifier=Modifier.height(10.dp))
                    Button(onClick = {
                        if(buttonText=="Turn on Internet") {
                            context.startActivity(Intent(android.provider.Settings.ACTION_NETWORK_OPERATOR_SETTINGS))
                            shouldRefresh = true
                            buttonText="Refresh"

                        }else{
                            shouldRefresh = true
                        }
                    }) {
                        Text(buttonText)
                    }
                }
            } else {
                LazyColumn(state = listState, modifier = Modifier.padding(3.dp)) {
                    items(blogs) { blog ->
                        BlogItem(blog, onBlogClick, context)
                    }
                    item {
                        if (viewModel.isFetching) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center,
                            ) {
                                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BlogItem(blog: BlogPostEntity, onBlogClick: (String) -> Unit,context: Context) {
    var showDialog by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth()
            .padding(8.dp)
            .background(color = Color.Transparent)
            .clickable {
                if (NetworkUtils.isInternetAvailable(context)) {
            onBlogClick(blog.link)
        } else {
            showDialog = true
        }
},
        colors = CardColors(containerColor = Color(0x23FFFFFF),
            contentColor = Color.White,
            disabledContentColor = Color.White,
            disabledContainerColor = Color(0x19A39999)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = decodeHtml(blog.title), fontSize = 16.sp
                )
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("No Internet Connection") },
            text = { Text("Please turn on your internet connection to view this blog.", fontSize = 15.sp) },
            confirmButton = {
                Button(onClick = {
                    context.startActivity(Intent(android.provider.Settings.ACTION_NETWORK_OPERATOR_SETTINGS))
                    showDialog = false
                }) {
                    Text("Turn On Internet")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
fun decodeHtml(htmlText: String): String {
    return HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
}
