package com.sultudy.waterfall.spike.jp.simple.server

import com.sun.net.httpserver.{Headers, HttpExchange, HttpHandler}

import scala.io.Source

class GetStreamHandler extends HttpHandler{
  override def handle(httpExchange: HttpExchange): Unit = {
    val headers : Headers = httpExchange.getResponseHeaders
    headers.add("Content-Type", "application/text")

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
