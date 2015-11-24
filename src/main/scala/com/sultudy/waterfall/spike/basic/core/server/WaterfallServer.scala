package com.sultudy.waterfall.spike.basic.core.server

import com.sultudy.waterfall.spike.basic.core.handler.WaterfallHandler

trait WaterfallServer {
  def start(handler: WaterfallHandler)
}
