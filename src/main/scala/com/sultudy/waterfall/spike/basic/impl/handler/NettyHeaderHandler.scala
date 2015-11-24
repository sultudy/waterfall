package com.sultudy.waterfall.spike.basic.impl.handler

import com.sultudy.waterfall.spike.basic.core.handler.HttpHeaderHandler
import com.sultudy.waterfall.spike.basic.core.socket.WaterfallSocket
import io.netty.handler.codec.http.DefaultHttpResponse
import io.netty.handler.codec.http.HttpResponseStatus._
import io.netty.handler.codec.http.HttpVersion._

class NettyHeaderHandler extends HttpHeaderHandler {
  def sendHeaders(socket: WaterfallSocket) = socket.writeHeaders(new DefaultHttpResponse(HTTP_1_1, OK))
}
