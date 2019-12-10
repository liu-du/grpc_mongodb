package blog.common

import io.grpc.ManagedChannelBuilder

abstract class Channel {
  val channel = ManagedChannelBuilder
    .forAddress("localhost", 50051)
    .usePlaintext
    .build
}
