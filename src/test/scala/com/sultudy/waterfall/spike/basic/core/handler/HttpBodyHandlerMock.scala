package com.sultudy.waterfall.spike.basic.core.handler

import com.sultudy.waterfall.spike.basic.core.socket.WaterfallSocket

class HttpBodyHandlerMock extends HttpBodyHandler {
  def sendBody(socket: WaterfallSocket) = socket.writeString("body")
}
