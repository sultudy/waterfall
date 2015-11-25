package com.sultudy.waterfall.spike.basic.impl.server

import java.net.InetSocketAddress
import java.nio.channels.{AsynchronousServerSocketChannel, AsynchronousSocketChannel, CompletionHandler}

import com.sultudy.waterfall.spike.basic.core.handler.WaterfallHandler
import com.sultudy.waterfall.spike.basic.core.server.WaterfallServer
import com.sultudy.waterfall.spike.basic.impl.socket.WaterfallAsynchronousSocketChannel

case class AsyncStreamServer(port: Int) extends WaterfallServer {
  def start(handler: WaterfallHandler) = {
    val serverSocket = AsynchronousServerSocketChannel.open()
    serverSocket.bind(new InetSocketAddress(port))
    serverSocket.accept(new Attachment(serverSocket), new AsyncRequestHandler(handler))
  }
}

class Attachment(val asynchronousServerSocketChannel: AsynchronousServerSocketChannel)

class AsyncRequestHandler(handler: WaterfallHandler) extends CompletionHandler[AsynchronousSocketChannel, Attachment] {
  def completed(socket: AsynchronousSocketChannel, attachment: Attachment) = {
    attachment.asynchronousServerSocketChannel.accept(attachment, this)
    handler.process(new WaterfallAsynchronousSocketChannel(socket))
  }

  def failed(e: Throwable, attachment: Attachment) = e.printStackTrace
}