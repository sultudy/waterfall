package com.sultudy.waterfall.spike

import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients

import scala.io.Source

object SimpleStreamClient extends App {

  val httpClient = HttpClients.createDefault()

  try {
    val httpGet = new HttpGet("http://localhost:8080/")
    println("Request-Line: " + httpGet.getRequestLine)
    val response = httpClient.execute(httpGet)
    response.getAllHeaders.foreach {
      h => println(h.getName + ": " + h.getValue)
    }
    if (response.getStatusLine.getStatusCode == 200) {
      println("----------------------------------------")
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
