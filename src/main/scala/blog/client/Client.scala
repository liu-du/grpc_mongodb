package blog.client

import blog.common.Channel
import blog.BlogServiceGrpc
import blog.Blog

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits._
import blog.ProtoString
import io.grpc.stub.StreamObserver
import blog.EmptyMessage
import java.util.concurrent.CountDownLatch

object Client extends Channel {
  val client = BlogServiceGrpc.stub(channel)

  def main(args: Array[String]): Unit = {

    val resultF1 = for {
      newBlog <- client.createBlog(Blog("", "jimmy", "hello", "content"))
      readBlog <- client.readBlog(ProtoString(newBlog.id))
      updatedBlog <- client.updateBlog(readBlog.copy(content = readBlog.content + " updated!"))
      // deletedId <- client.deleteBlog(updatedBlog)
    } yield (newBlog, readBlog, updatedBlog)


    val resultF2 = for {
      newBlog <- client.createBlog(Blog("", "ludi", "hello2", "content2"))
      readBlog <- client.readBlog(ProtoString(newBlog.id))
      updatedBlog <- client.updateBlog(readBlog.copy(content = readBlog.content + " updated!"))
      // deletedId <- client.deleteBlog(updatedBlog)
    } yield (newBlog, readBlog, updatedBlog)

    val result1 = Await.result(resultF1, 10.seconds)
    println("result:")
    println(result1.productIterator.mkString("\n"))

    val result2 = Await.result(resultF2, 10.seconds)
    println("result:")
    println(result2.productIterator.mkString("\n"))

    val latch = new CountDownLatch(1)

    val responseObserver = new StreamObserver[Blog] {
      def onNext(blog: Blog): Unit = {
        println("streaming")
        println(blog)
      }
      def onCompleted(): Unit = {
        println("streaming finished.")
        latch.countDown()
      }
      def onError(t: Throwable): Unit = ()
    }

    client.listBlog(EmptyMessage(), responseObserver)

    latch.await()
  }
}
