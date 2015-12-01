package com.sultudy.waterfall.spike.jp.simple.server

import java.io.{BufferedInputStream, File, FileInputStream, OutputStream}

import com.sun.net.httpserver.{Headers, HttpExchange, HttpHandler}

class GetHandler extends HttpHandler{
  override def handle(httpExchange: HttpExchange): Unit = {
    val headers : Headers = httpExchange.getResponseHeaders
    headers.add("Content-Type", "application/text")

    val file : File = new File(getClass.getResource("/spike/messages.txt").getPath)
    val bytearray = Array.ofDim[Byte](file.length.toInt)
    val fis : FileInputStream = new FileInputStream(file)
    val bis = new BufferedInputStream(fis)
    bis.read(bytearray, 0, bytearray.length)

    httpExchange.sendResponseHeaders(200, file.length())
    val os : OutputStream = httpExchange.getResponseBody
    os.write(bytearray, 0, bytearray.length)
    os.close
  }
}
