package com.example.blogreadervrid

import BlogRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class BlogViewModel(private val repository: BlogRepository) : ViewModel() {
    private val _blogs = MutableStateFlow<List<BlogPostEntity>>(emptyList())
    val blogs: StateFlow<List<BlogPostEntity>> = _blogs

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
                val newBlogs = repository.getBlogPosts(page = currentPage)
                _blogs.value = _blogs.value + newBlogs
                currentPage++
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isFetching = false
            }
        }
    }
}
