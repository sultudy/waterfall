package com.sultudy.waterfall.spike.basic.core.handler

import com.sultudy.waterfall.spike.basic.core.socket.WaterfallSocket

class HttpHeaderHandlerMock extends HttpHeaderHandler {
  def sendHeaders(socket: WaterfallSocket) = socket.writeHeaders("headers\r\n\r\n")
}
