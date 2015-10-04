package com.sultudy.waterfall.spike

import java.net.InetSocketAddress

import com.sun.net.httpserver.{HttpExchange, HttpHandler, HttpServer}

import scala.io.Source

object SimpleStreamServer extends App {
  val server = HttpServer.create(new InetSocketAddress(8080), 0)
  server.createContext("/", new RequestHandler)
  server.start()
  println("Hit any key to exit...")
  System.in.read()
  server.stop(0)
}

class RequestHandler extends HttpHandler {

  override def handle(httpExchange: HttpExchange) = {
    // If the response length parameter is zero,
    // then chunked transfer encoding is used and an arbitrary amount of data may be sent.
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
