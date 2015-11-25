package com.sultudy.waterfall.spike

import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.{SelectionKey, Selector, ServerSocketChannel, SocketChannel}

import scala.io.Source

object SimpleNioNonblockingStreamServer extends App {
  val selector = Selector.open()
  val serverSocketChannel = ServerSocketChannel.open()

  if (serverSocketChannel.isOpen && selector.isOpen) {
    initServer
    while (true) {
      try {
        process
      } catch {
        case e: Exception => e.printStackTrace
      }
    }

    def initServer = {
      serverSocketChannel.configureBlocking(false)
      serverSocketChannel.bind(new InetSocketAddress(8080))
      serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT)
    }

    def process = {
      if (selector.select() != 0) {
        val keys = selector.selectedKeys().iterator()
        while (keys.hasNext) {
          val key = keys.next()
          keys.remove()
          if (key.isValid)
            if (key.isAcceptable) acceptOp
            else if (key.isReadable) readOp(key)
            else if (key.isWritable) writeOp(key)
        }
      }
    }
  }

  private def acceptOp = {
    val socketChannel = serverSocketChannel.accept()
    socketChannel.configureBlocking(false)
    socketChannel.register(selector, SelectionKey.OP_READ)
  }

  private def readOp(key: SelectionKey) = {
    key.channel() match {
      case s: SocketChannel =>
        try {
          readOp(s)
        } catch {
          case e: Exception => {
            e.printStackTrace
            close(s, key)
          }
        }
    }

    def readOp(socketChannel: SocketChannel) = {
      val numRead = socketChannel.read(ByteBuffer.allocate(2 * 1024))
      if (numRead > 0) key.interestOps(SelectionKey.OP_WRITE)
      else close(socketChannel, key)
    }
  }

  private def writeOp(key: SelectionKey) = {
    key.channel() match {
      case s: SocketChannel =>
        try {
          writeOp(s)
        } catch {
          case e: Exception => {
            e.printStackTrace
            close(s, key)
          }
        }
    }

    def writeOp(socket: SocketChannel) = {

      sendHeaders
      sendBody
      close

      def sendHeaders = {
        socket.write(ByteBuffer.wrap("HTTP/1.1 200 OK\r\n".getBytes))
        socket.write(ByteBuffer.wrap("\r\n".getBytes))
      }

      def sendBody = {
        val br = Source.fromURL(getClass.getResource("/spike/messages.txt")).bufferedReader()
        Stream.continually(br.readLine())
          .takeWhile(_ != null)
          .map(_ + "\r\n")
          .foreach { line =>
            socket.write(ByteBuffer.wrap(line.getBytes))
            // sleep short time for test
            Thread sleep 100
          }
        br.close()
      }

      def close = socket.close
    }
  }

  private def close(socketChannel: SocketChannel, key: SelectionKey) = {
    socketChannel.close()
    key.cancel()
  }
}
