package blog.server

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._
import blog.BlogServiceGrpc
import blog.Blog
import blog.dao.Mongo

object BlogServiceImpl extends BlogServiceGrpc.BlogService {
  def createBlog(blog: Blog): Future[Blog] = Future {
    Mongo.createBlog(blog)
  }
}
