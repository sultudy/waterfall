package com.sultudy.waterfall.spike.basic.core.handler

import com.sultudy.waterfall.spike.basic.core.socket.WaterfallSocket

class WaterfallThreadHandler(handler: WaterfallHandler, socket: WaterfallSocket) extends Runnable {
  def run() = handler.process(socket)
}
