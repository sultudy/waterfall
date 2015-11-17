package com.sultudy.waterfall.spike.akka.server

import akka.actor.{Actor, ActorLogging, Props}
import com.sun.net.httpserver.HttpExchange

import scala.io.Source

class StreamServer extends Actor with ActorLogging {

  import StreamServer._

  override def receive: Receive = {
    case Start(httpExchange) =>
      log.info("received Start message. remote:<{}:{}>",
        httpExchange.getRemoteAddress.getHostName, httpExchange.getRemoteAddress.getPort)
      httpExchange.sendResponseHeaders(200, 0)
      sendChunkedMessages(httpExchange)
  }

  private def sendChunkedMessages(httpExchange: HttpExchange): Unit = {
    val os = httpExchange.getResponseBody
    val br = Source.fromURL(getClass.getResource("/spike/messages.txt")).bufferedReader()
    Stream.continually(br.readLine())
      .takeWhile(_ != null)
      .map(s => s + "\r\n")
      .foreach { line =>
      os.write(line.getBytes)
      os.flush()
      // sleep short time for test
      Thread sleep 100
    }
    br.close()
    os.close()
  }
}

object StreamServer {

  def props = Props(classOf[StreamServer])

  case class Start(httpExchange: HttpExchange)
}
