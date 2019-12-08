package blog.client

import blog.common.Channel
import blog.BlogServiceGrpc
import blog.Blog

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits._

object Client extends Channel {
  def main(args: Array[String]): Unit = {
    val client = BlogServiceGrpc.stub(channel)
    val resp = client
      .createBlog(Blog("101", "1", "hello", "content"))
      .map(println)

    val _ = Await.ready(resp, 10.seconds)
  }
}
