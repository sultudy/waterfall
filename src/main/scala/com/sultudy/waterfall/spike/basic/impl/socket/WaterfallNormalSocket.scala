package com.sultudy.waterfall.spike.basic.impl.socket

import java.net.Socket

import com.sultudy.waterfall.spike.basic.core.socket.WaterfallSocket

class WaterfallNormalSocket(socket: Socket) extends WaterfallSocket {
  def writeString(str: String) = socket.getOutputStream.write(str.getBytes)

  def close = socket.close
}
