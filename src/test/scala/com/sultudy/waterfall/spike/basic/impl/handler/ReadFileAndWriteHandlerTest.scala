package com.sultudy.waterfall.spike.basic.impl.handler

import com.sultudy.waterfall.spike.basic.core.socket.WaterfallSocketMock
import org.scalatest.FunSpec

class ReadFileAndWriteHandlerTest extends FunSpec {
  describe("A ReadFileAndWriteHandler") {
    describe("when call process") {
      val handler = new ReadFileAndWriteHandler("/spike/test.txt")
      val socketMock = new WaterfallSocketMock
      handler.sendBody(socketMock)
      it("should read file and write with default line delimited") {
        assert(socketMock.writed == expected)
        def expected: String = {
          "123\r\n" +
          "abc\r\n"
        }
      }
    }
  }
}
