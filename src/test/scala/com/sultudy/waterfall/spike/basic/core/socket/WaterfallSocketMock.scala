package com.sultudy.waterfall.spike.basic.core.socket

class WaterfallSocketMock extends WaterfallSocket {
  var writed: String = ""
  var isClosed = false

  def writeString(str: String) = writed += str

  def close = isClosed = true
}
