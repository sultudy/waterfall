package com.sultudy.waterfall.spike.basic.impl.server

import com.sultudy.waterfall.spike.basic.core.handler.WaterfallHandler
import com.sultudy.waterfall.spike.basic.core.server.WaterfallServer
import com.sultudy.waterfall.spike.basic.impl.socket.NettyChannelHandlerContext
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.{ChannelHandlerContext, ChannelInitializer, SimpleChannelInboundHandler}
import io.netty.handler.codec.http.{FullHttpRequest, HttpObjectAggregator, HttpServerCodec}
import io.netty.handler.logging.{LogLevel, LoggingHandler}

case class NettyStreamServer(port: Int) extends WaterfallServer {
  def start(handler: WaterfallHandler) = {
    val bossGroup = new NioEventLoopGroup(1)
    val workerGroup = new NioEventLoopGroup

    val b = new ServerBootstrap
    b.group(bossGroup, workerGroup)
      .channel(classOf[NioServerSocketChannel])
      .handler(new LoggingHandler(LogLevel.INFO))
      .childHandler(new SimpleNettyStreamServerInitializer(handler))

    b.bind(8080).sync().channel().closeFuture()
  }

  class SimpleNettyStreamServerInitializer(handler: WaterfallHandler) extends ChannelInitializer[SocketChannel] {
    def initChannel(ch: SocketChannel) = {
      val pipeline = ch.pipeline
      pipeline.addLast(new HttpServerCodec)
      pipeline.addLast(new HttpObjectAggregator(65536))
      pipeline.addLast(new SimpleNettyStreamServerHandler(handler))
    }
  }

  class SimpleNettyStreamServerHandler(handler: WaterfallHandler) extends SimpleChannelInboundHandler[FullHttpRequest] {
    def channelRead0(ctx: ChannelHandlerContext, msg: FullHttpRequest) =
      handler.process(new NettyChannelHandlerContext(ctx))
  }
}
