package com.sultudy.waterfall.spike.basic.impl.socket

import java.nio.ByteBuffer
import java.nio.channels.AsynchronousSocketChannel

import com.sultudy.waterfall.spike.basic.core.socket.WaterfallSocket

class WaterfallAsynchronousSocketChannel(socket: AsynchronousSocketChannel) extends WaterfallSocket {
  def writeString(str: String) = socket.write(ByteBuffer.wrap(str.getBytes))

  def close = socket.close
}