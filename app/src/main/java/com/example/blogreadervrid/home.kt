package com.example.blogreadervrid

import android.annotation.SuppressLint
import android.provider.CalendarContract.Colors
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BlogListScreen(viewModel: BlogViewModel, onBlogClick: (String) -> Unit) {
    val blogs by viewModel.blogs.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                if (visibleItems.isNotEmpty() && visibleItems.last().index == blogs.size - 1) {
                    viewModel.fetchBlogs()
                }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Top Blogs", color = Color.White) }, colors = TopAppBarColors(containerColor = Color(
                0xA61C1C1C
            ), titleContentColor = Color.White, scrolledContainerColor = Color.Black, actionIconContentColor = Color.Black, navigationIconContentColor = Color.Black))
        },
        containerColor = Color.Black
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(color = Color.Black)
        ) {
            LazyColumn(state = listState, modifier = Modifier.padding(3.dp)) {
                items(blogs) { blog ->
                    BlogItem(blog, onBlogClick)
                }

                item {
                    if (viewModel.isFetching) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun BlogItem(blog: BlogPost, onBlogClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth()
            .padding(8.dp)
            .background(color = Color.Transparent)
            .clickable { onBlogClick(blog.link) },
        colors = CardColors(containerColor = Color(0x23FFFFFF),
            contentColor = Color.White,
            disabledContentColor = Color.White,
            disabledContainerColor = Color(0x19A39999)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = decodeHtml(blog.title.rendered), fontSize = 16.sp
                )
        }
    }
}
fun decodeHtml(htmlText: String): String {
    return HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
}