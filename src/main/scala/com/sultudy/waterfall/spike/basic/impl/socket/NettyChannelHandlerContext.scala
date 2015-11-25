package com.sultudy.waterfall.spike.basic.impl.socket

import com.sultudy.waterfall.spike.basic.core.socket.WaterfallSocket
import io.netty.buffer.Unpooled
import io.netty.channel.{ChannelFutureListener, ChannelHandlerContext}
import io.netty.handler.codec.http.DefaultHttpResponse
import io.netty.util.CharsetUtil

class NettyChannelHandlerContext(ctx: ChannelHandlerContext) extends WaterfallSocket {
  override def writeHeaders(headers: Object) = headers match {
    case h: DefaultHttpResponse => ctx.writeAndFlush(h)
  }

  def writeString(str: String) = ctx.writeAndFlush(Unpooled.copiedBuffer(str, CharsetUtil.UTF_8))

  def close = ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE)
}
