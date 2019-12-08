package blog.common

import io.grpc.ManagedChannelBuilder

abstract class Channel {
  val channel = ManagedChannelBuilder
    .forAddress("localhost", 50051)
    .usePlaintext()
    .build()
  // NettyChannelBuilder
  //   .forAddress("localhost", 50051)
  //   .sslContext(
  //     GrpcSslContexts.forClient().trustManager(new File("ssl/ca.crt")).build()
  //   )
  //   .build()
}
