package com.sultudy.waterfall.spike.basic.core.handler

import com.sultudy.waterfall.spike.basic.core.socket.WaterfallSocket

trait HttpHeaderHandler {
  def sendHeaders(socket: WaterfallSocket)
}
