package com.sultudy.waterfall.spike.basic.impl.handler

import com.sultudy.waterfall.spike.basic.core.handler.HttpBodyHandler
import com.sultudy.waterfall.spike.basic.core.socket.WaterfallSocket

import scala.io.Source

class ReadFileAndWriteHandler(file: String, delimited: String) extends HttpBodyHandler {
  def this(file: String) = this(file, "line")

  val delimiter: String = {
    delimited match {
      case "line" => "\r\n"
    }
  }

  def sendBody(socket: WaterfallSocket) = {
    val br = Source.fromURL(getClass.getResource(file)).bufferedReader()
    Stream.continually(br.readLine)
      .takeWhile(_ != null)
      .foreach { line =>
        socket.writeString(line)
        socket.writeString(delimiter)
        // sleep short time for test
        Thread sleep 100
      }
    br.close
  }
}
