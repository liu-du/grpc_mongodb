package com.liudu.blog.server

import scala.concurrent.ExecutionContext.global
import io.grpc.ServerBuilder
import blog.BlogServiceGrpc
import blog.server.BlogServiceImpl

object BlogServer {
  def main(args: Array[String]): Unit = {
    val server = ServerBuilder
      .forPort(50051)
      .addService(BlogServiceGrpc.bindService(BlogServiceImpl, global))
      .build()
      .start()

    sys.addShutdownHook {
      println("shutting down grpc server...")
      server.shutdown()
    }

    server.awaitTermination()

  }
}
