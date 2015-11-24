package com.sultudy.waterfall.spike.basic.core.socket

trait WaterfallSocket {
  def writeHeaders(headers: Object) = headers match {
    case h: String => writeString(h)
  }

  def writeString(str: String)

  def close
}
