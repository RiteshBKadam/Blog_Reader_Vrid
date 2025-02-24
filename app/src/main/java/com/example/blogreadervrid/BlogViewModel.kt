package com.example.blogreadervrid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BlogViewModel : ViewModel() {
    private val _blogs = MutableStateFlow<List<BlogPost>>(emptyList())
    val blogs: StateFlow<List<BlogPost>> = _blogs

    private var currentPage = 1
    var isFetching = false
        private set

    init {
        fetchBlogs()
    }

    fun fetchBlogs() {
        if (isFetching) return
        isFetching = true

        viewModelScope.launch {
            try {
                val newBlogs = RetrofitInstance.api.getBlogs(perPage = 10, page = currentPage)
                _blogs.value += newBlogs
                currentPage++
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isFetching = false
            }
        }
    }
}
