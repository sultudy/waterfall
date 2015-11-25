package com.sultudy.waterfall.spike

import java.net.{ServerSocket, Socket}

import scala.io.Source

object SimpleThreadStreamServer extends App {
  val serverSocket = new ServerSocket(8080)
  while (true) {
    val socket = serverSocket.accept()
    (new Thread(new ThreadHandler(socket))).start()
  }
}

class ThreadHandler(socket: Socket) extends Runnable {
  def run() {
    val os = socket.getOutputStream
    os.write("HTTP/1.1 200 OK\r\n".getBytes)
    os.write("\r\n".getBytes)
    sendBody

    def sendBody = {
      val br = Source.fromURL(getClass.getResource("/spike/messages.txt")).bufferedReader()
      Stream.continually(br.readLine())
        .takeWhile(_ != null)
        .map(_ + "\r\n")
        .foreach { line =>
          os.write(line.getBytes)
          // sleep short time for test
          Thread sleep 100
        }
      br.close()
    }

    os.close
  }
}