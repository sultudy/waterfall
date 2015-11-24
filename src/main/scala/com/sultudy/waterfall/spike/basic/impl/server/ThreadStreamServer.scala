package com.sultudy.waterfall.spike.basic.impl.server

import java.net.ServerSocket

import com.sultudy.waterfall.spike.basic.core.handler.{WaterfallHandler, WaterfallThreadHandler}
import com.sultudy.waterfall.spike.basic.core.server.WaterfallServer
import com.sultudy.waterfall.spike.basic.core.socket.WaterfallSocket
import com.sultudy.waterfall.spike.basic.impl.socket.WaterfallNormalSocket

case class ThreadStreamServer(port: Int) extends WaterfallServer {
  def start(handler: WaterfallHandler) =
    new Thread(ThreadStreamServerAcceptor(new ServerSocket(8080), handler)).start

  case class ThreadStreamServerAcceptor(serverSocket: ServerSocket, handler: WaterfallHandler) extends Runnable {
    def run() = while (true)
      process(new WaterfallNormalSocket(serverSocket.accept()), handler)

    def process(socket: WaterfallSocket, handler: WaterfallHandler) =
      new Thread(new WaterfallThreadHandler(handler, socket)).start
  }
}
