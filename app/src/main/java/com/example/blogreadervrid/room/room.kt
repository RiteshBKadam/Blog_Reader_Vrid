package com.example.blogreadervrid

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blog_posts")
data class BlogPostEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val content: String,
    val link: String
)
