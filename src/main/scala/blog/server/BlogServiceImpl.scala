package blog.server

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._
import com.mongodb.client.MongoClient
import blog.dao.Mongo
import blog.BlogServiceGrpc.BlogService
import blog.{Blog, ProtoString}

object BlogServiceImpl extends BlogService {
  val mongo: Mongo = Mongo()
  def readBlog(request: ProtoString): Future[Blog] =
    Future {
      mongo
        .readBlog(request.id)
        .getOrElse(throw new Exception("Blog not found"))
    }

  def createBlog(blog: Blog): Future[Blog] =
    Future(mongo.createBlog(blog))

}
