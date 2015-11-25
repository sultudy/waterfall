package com.sultudy.waterfall.spike.basic.main

import com.sultudy.waterfall.spike.basic.core.handler.{WaterfallHandler, HttpBodyHandler}
import com.sultudy.waterfall.spike.basic.core.server.WaterfallServer
import com.sultudy.waterfall.spike.basic.impl.handler._
import com.sultudy.waterfall.spike.basic.impl.server.{AsyncStreamServer, ThreadStreamServer, NettyStreamServer}

object WaterfallServerApp extends App {
  val port = 8080
  start(port)
  println("Hit any key to exit...")
  System.in.read
  System.exit(0)

  def start(port: Int) = {
//    startServer(ThreadStreamServer(port))
//    startServer(AsyncStreamServer(port))
    startServer(NettyStreamServer(port))
  }

  def startServer(server: WaterfallServer) = server match {
    case ThreadStreamServer(_) =>
      server.start(WaterfallHandler(new SocketHeaderHandler, bodyHandler))
    case AsyncStreamServer(_) =>
      server.start(WaterfallHandler(new SocketHeaderHandler, bodyHandler))
    case NettyStreamServer(_) =>
      server.start(WaterfallHandler(new NettyHeaderHandler, bodyHandler))
  }

  def bodyHandler: HttpBodyHandler = {
    new ReadFileAndWriteHandler("/spike/messages.txt")
  }
}
