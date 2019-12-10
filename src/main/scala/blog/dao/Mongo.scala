package blog.dao

import org.json4s.Formats
import org.json4s.native.JsonMethods._
import com.mongodb.client.{MongoClient, MongoClients}
import com.mongodb.client.model.Filters.{eq => mongoEq}

import blog.Blog
import org.bson.Document
import org.bson.types.ObjectId
import collection.JavaConverters._

case class Mongo(
  mongo: MongoClient = MongoClients.create("mongodb://jimmy:pass@localhost:27017/blog")
  )(implicit formats: Formats) {

  private val blogCollection = mongo
    .getDatabase("blog")
    .getCollection("blogs")

  def createBlog(blog: Blog): Blog = {
    val doc = new Document("authorId", blog.authorId)
      .append("title", blog.title)
      .append("content", blog.content)

    blogCollection.insertOne(doc)

    blog.copy(id = doc.getObjectId("_id").toString)
  }

  def readBlog(id: String): Option[Blog] = 
    for {
      doc <- Option(blogCollection.find(mongoEq("_id", new ObjectId(id))).first)
      blog <- parse(doc.toJson).extractOpt[Blog]
    } yield blog.copy(id = doc.getObjectId("_id").toString)

  def updateBlog(blog: Blog): Option[Blog] = {

    val doc = new Document("authorId", blog.authorId)
      .append("title", blog.title)
      .append("content", blog.content)

    val updateResult = blogCollection.replaceOne(mongoEq("_id", new ObjectId(blog.id)), doc)
    if (updateResult.getModifiedCount() == 1) Some(blog)
    else None
  }

  def deleteBlog(blog: Blog): Option[String] = {
    val deleteResult = blogCollection.deleteOne(mongoEq("_id", new ObjectId(blog.id)))
    if (deleteResult.getDeletedCount == 0) None
    else Some(blog.id)
  }

  def listBlogs: Iterable[Blog] = 
    for {
      doc <- blogCollection.find().asScala
      blog <- parse(doc.toJson).extractOpt[Blog]
    } yield blog.copy(id = doc.getObjectId("_id").toString)
}

object Mongo {
  import org.json4s.DefaultFormats
  implicit val defaultFormats = DefaultFormats
}