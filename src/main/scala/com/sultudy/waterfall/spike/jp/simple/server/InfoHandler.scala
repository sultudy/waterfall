package com.sultudy.waterfall.spike.jp.simple.server

import java.io.OutputStream

import com.sun.net.httpserver.{HttpExchange, HttpHandler}

class InfoHandler extends HttpHandler {
  override def handle(httpExchange: HttpExchange): Unit = {
    val response : String = "Basic HttpServer. Through /get uri U can download text file"
    httpExchange.sendResponseHeaders(200, response.length())

    val outputStream : OutputStream = httpExchange.getResponseBody()
    outputStream.write(response.getBytes())
    outputStream.close()
  }
}
