import com.example.blogreadervrid.BlogApiService
import com.example.blogreadervrid.BlogPostEntity
import com.example.blogreadervrid.room.BlogPostDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BlogRepository(private val api: BlogApiService, private val dao: BlogPostDao) {

    suspend fun getBlogPosts(page: Int): List<BlogPostEntity> {
        return withContext(Dispatchers.IO) {
            try {
                val apiPosts = api.getBlogs(perPage = 10, page = page)
                val entities = apiPosts.map { BlogPostEntity(it.id, it.title.rendered, it.content.rendered, it.link) }
                dao.insertAll(entities)
                return@withContext entities
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext dao.getAllPosts()
            }
        }
    }
}
