package com.sultudy.waterfall.spike.jp.simple.server

import java.net.InetSocketAddress

import com.sun.net.httpserver.HttpServer

object BasicHttpServer extends App {
  val server : HttpServer = HttpServer.create(new InetSocketAddress(8000), 0)
  server.createContext("/info", new InfoHandler)
  server.createContext("/get", new GetHandler)
  server.createContext("/get/stream", new GetStreamHandler)
  server.setExecutor(null)

  server.start()
  println("Hit any key to exit...")
  System.in.read()
  server.stop(0)
}
