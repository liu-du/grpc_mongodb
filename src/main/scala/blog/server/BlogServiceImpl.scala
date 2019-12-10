package blog.server

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._
import com.mongodb.client.MongoClient
import io.grpc.stub.StreamObserver
import blog.BlogServiceGrpc.BlogService
import blog.{Blog, ProtoString, EmptyMessage}
import blog.dao.Mongo
import blog.dao.Mongo.defaultFormats

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

  def updateBlog(request: Blog): Future[Blog]  = 
    Future { 
      mongo
        .updateBlog(request)
        .getOrElse(throw new Exception("Blog not found"))
    }

  def deleteBlog(request: Blog): Future[ProtoString] = 
    Future {
      mongo
        .deleteBlog(request)
        .map(ProtoString(_))
        .getOrElse(throw new Exception("Blog not found"))
    }

  def listBlog(request: EmptyMessage, responseObserver: StreamObserver[Blog]): Unit = {
    mongo.listBlogs.foreach { blog =>
      Thread.sleep(1000L)
      responseObserver.onNext(blog)
    }
    responseObserver.onCompleted()
  }
}
