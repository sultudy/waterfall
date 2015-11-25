package com.sultudy.waterfall.spike

import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.{AsynchronousServerSocketChannel, AsynchronousSocketChannel, CompletionHandler}

import scala.io.Source

object SimpleNioAsynchronousStreamServer extends App {
  val asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open()
  asynchronousServerSocketChannel.bind(new InetSocketAddress(8080))
  asynchronousServerSocketChannel.accept(new Attachment(asynchronousServerSocketChannel), new AsyncRequestHandler())
  println("Hit any key to exit...")
  System.in.read()
}

class Attachment(val asynchronousServerSocketChannel: AsynchronousServerSocketChannel)

class AsyncRequestHandler extends CompletionHandler[AsynchronousSocketChannel, Attachment] {
  override def completed(socket: AsynchronousSocketChannel, attachment: Attachment) = {
    attachment.asynchronousServerSocketChannel.accept(attachment, this)
    
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

  override def failed(e: Throwable, attachment: Attachment) = e.printStackTrace
}

