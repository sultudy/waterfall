package com.sultudy.waterfall.spike.basic.core.handler

import com.sultudy.waterfall.spike.basic.core.socket.WaterfallSocketMock
import org.scalatest.FunSpec

class WaterfallHandlerTest extends FunSpec {
  describe("A WaterfallHandler") {
    describe("when call process") {
      val headerHandlerMock = new HttpHeaderHandlerMock
      val bodyHandlerMock = new HttpBodyHandlerMock
      val socketMock = new WaterfallSocketMock
      val handler = WaterfallHandler(headerHandlerMock, bodyHandlerMock)
      handler.process(socketMock)
      it("should send Headers and Body") {
        assert(socketMock.writed == "headers\r\n\r\nbody")
      }
      it("should close socket") {
        assert(socketMock.isClosed)
      }
    }
  }
}
