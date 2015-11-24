package com.sultudy.waterfall.spike.akka.client

import akka.actor.{ActorLogging, Actor, Props}
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients

import scala.io.Source

class StreamClient extends Actor with ActorLogging {

  import StreamClient._

  override def receive: Receive = {
    case Start =>
      log.info("received Start message.")
      startStream()
      self ! End
    case End =>
      log.info("stream finished.")
      context.stop(self)
  }

  private def startStream(): Unit = {
    val httpClient = HttpClients.createDefault()
    try {
      val httpGet = new HttpGet("http://localhost:8080/")
      log.info("Request-Line: {}", httpGet.getRequestLine)
      val response = httpClient.execute(httpGet)
      response.getAllHeaders.foreach {
        h => log.info("header<{}>:value<{}>", h.getName, h.getValue)
      }
      if (response.getStatusLine.getStatusCode == 200) {
        log.info("----------------------------------------")
        val br = Source.fromInputStream(response.getEntity.getContent).bufferedReader()
        Stream.continually(br.readLine())
          .takeWhile(_ != null)
          .foreach(s => println(s))
        br.close()
      }
    } finally {
      httpClient.close()
    }
  }
}

object StreamClient {

  def props = Props[StreamClient]

  case object Start
  case object End
}
