package com.sultudy.waterfall.spike.basic.core.handler

import com.sultudy.waterfall.spike.basic.core.socket.WaterfallSocket

case class WaterfallHandler(headerHandler: HttpHeaderHandler, bodyHandler: HttpBodyHandler) {
  def process(socket: WaterfallSocket) = {
    headerHandler.sendHeaders(socket)
    bodyHandler.sendBody(socket)
    socket.close
  }
}
