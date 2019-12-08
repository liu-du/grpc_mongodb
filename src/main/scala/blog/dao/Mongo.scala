package blog.dao

import org.json4s._
import org.json4s.native.JsonMethods._
import com.mongodb.client.{MongoClient, MongoClients}
import com.mongodb.client.model.Filters.{eq => mongoEq}

import blog.Blog
import org.bson.Document
import org.bson.types.ObjectId

case class Mongo(
    mongo: MongoClient =
      MongoClients.create("mongodb://jimmy:pass@localhost:27017/blog")
) {

  private val blogCollection = mongo
    .getDatabase("blog")
    .getCollection("blogs")

  def createBlog(blog: Blog): Blog = {
    val doc = new Document("authorId", blog.authorId)
      .append("title", blog.title)
      .append("content", blog.content + " saved!")

    blogCollection.insertOne(doc)

    blog.copy(id = doc.getObjectId("_id").toString)
  }

  def readBlog(
      id: String
  )(implicit formats: Formats = DefaultFormats): Option[Blog] = {
    Option {
      blogCollection.find(mongoEq("_id", new ObjectId(id))).first
    }.map(_.toJson)
      .map(parse(_))
      .flatMap(_.extractOpt[Blog])
  }
}
