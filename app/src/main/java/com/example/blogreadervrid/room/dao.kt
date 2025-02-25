package com.example.blogreadervrid.room
import androidx.room.*
import com.example.blogreadervrid.BlogPostEntity

@Dao
interface BlogPostDao {
    @Query("SELECT * FROM blog_posts")
    suspend fun getAllPosts(): List<BlogPostEntity> // Get all saved blogs

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<BlogPostEntity>) // Save blogs

    @Query("DELETE FROM blog_posts")
    suspend fun clearAll() // Clear old data
}
