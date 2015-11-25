package com.sultudy.waterfall.spike.basic.core.handler

import com.sultudy.waterfall.spike.basic.core.socket.WaterfallSocket

trait HttpBodyHandler {
  def sendBody(socket: WaterfallSocket)
}
