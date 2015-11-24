package com.sultudy.waterfall.spike.basic.impl.handler

import com.sultudy.waterfall.spike.basic.core.handler.HttpHeaderHandler
import com.sultudy.waterfall.spike.basic.core.socket.WaterfallSocket

class SocketHeaderHandler extends HttpHeaderHandler {
  def sendHeaders(socket: WaterfallSocket) = {
    socket.writeString("HTTP/1.1 200 OK\r\n")
    socket.writeString("\r\n")
  }
}
