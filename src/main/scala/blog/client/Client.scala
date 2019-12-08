package blog.client

import blog.common.Channel
import blog.BlogServiceGrpc
import blog.Blog

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits._
import blog.ProtoString

object Client extends Channel {
  def main(args: Array[String]): Unit = {
    val client = BlogServiceGrpc.stub(channel)
    val resp = client
      .createBlog(Blog("101", "1", "hello", "content"))

    val resp2 = resp.flatMap { blog =>
      client.readBlog(ProtoString(blog.id))
    }

    val blog1 = Await.ready(resp, 10.seconds)
    println(s"blog 1: $blog1")

    val blog2 = Await.ready(resp2, 10.seconds)
    println(s"blog 2: $blog2")
  }
}
