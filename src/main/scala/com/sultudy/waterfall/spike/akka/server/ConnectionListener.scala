package com.sultudy.waterfall.spike.akka.server

import java.net.InetSocketAddress

import akka.actor.ActorSystem
import com.sun.net.httpserver.{HttpExchange, HttpHandler, HttpServer}

/**
 * - client의 접속을 기다린다.
 * - 요청이 오면 actor를 만들고, actor에게 connection을 던진다.
 */
object ConnectionListener extends App {

  val system = ActorSystem("stream-server-world")

  val server = HttpServer.create(new InetSocketAddress(8080), 0)
  server.createContext("/", new RequestHandler(system))
  server.start()
  println("ConnectionListener started....")
}

class RequestHandler(system: ActorSystem) extends HttpHandler {

  override def handle(httpExchange: HttpExchange): Unit = {
    system.actorOf(StreamServer.props) ! StreamServer.Start(httpExchange)
  }
}
