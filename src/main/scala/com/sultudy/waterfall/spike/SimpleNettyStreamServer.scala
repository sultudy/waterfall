package com.sultudy.waterfall.spike

import io.netty.bootstrap.ServerBootstrap
import io.netty.buffer.Unpooled
import io.netty.channel._
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.http.HttpResponseStatus.OK
import io.netty.handler.codec.http.HttpVersion.HTTP_1_1
import io.netty.handler.codec.http._
import io.netty.handler.logging.{LogLevel, LoggingHandler}
import io.netty.util.CharsetUtil

import scala.io.Source

object SimpleNettyStreamServer extends App {
  val bossGroup = new NioEventLoopGroup(1)
  val workerGroup = new NioEventLoopGroup

  val b = new ServerBootstrap
  b.group(bossGroup, workerGroup)
    .channel(classOf[NioServerSocketChannel])
    .handler(new LoggingHandler(LogLevel.INFO))
    .childHandler(new SimpleNettyStreamServerInitializer)

  b.bind(8080).sync().channel().closeFuture().sync()
}

class SimpleNettyStreamServerInitializer extends ChannelInitializer[SocketChannel] {
  override def initChannel(ch: SocketChannel) = {
    val pipeline = ch.pipeline
    pipeline.addLast(new HttpServerCodec)
    pipeline.addLast(new HttpObjectAggregator(65536))
    pipeline.addLast(new SimpleNettyStreamServerHandler)
  }
}

class SimpleNettyStreamServerHandler extends SimpleChannelInboundHandler[FullHttpRequest] {
  override def channelRead0(ctx: ChannelHandlerContext, msg: FullHttpRequest) = {
    val response = new DefaultHttpResponse(HTTP_1_1, OK)
    ctx.writeAndFlush(response)

    val br = Source.fromURL(getClass.getResource("/spike/messages.txt")).bufferedReader()
    Stream.continually(br.readLine())
      .takeWhile(_ != null)
      .map(_ + "\r\n")
      .foreach { line =>
        ctx.writeAndFlush(Unpooled.copiedBuffer(line, CharsetUtil.UTF_8))
        // sleep short time for test
        Thread sleep 100
      }

    ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE)
  }
}
