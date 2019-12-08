package blog.dao
import com.mongodb.client.MongoClients
import blog.Blog
import org.bson.Document

object Mongo {
  val mongo = MongoClients.create("mongodb://jimmy:pass@localhost:27017/blog")
  val database = mongo.getDatabase("blog")
  val collection = database.getCollection("blogs")

  def createBlog(blog: Blog): Blog = {
    val doc = new Document("author_id", blog.authorId)
      .append("title", blog.title)
      .append("content", blog.content)

    collection.insertOne(doc)

    blog.copy(id = doc.getObjectId("_id").toString)
  }
}
