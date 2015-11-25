package com.sultudy.waterfall.spike

import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients

import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source
import scala.util.{Failure, Success}

object SimpleStreamClient extends App {
  val executeCount = 10
  implicit val ec = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(executeCount))
  val successCount = new AtomicInteger(0)
  val failCount = new AtomicInteger(0)
  val startTime = System.currentTimeMillis

  startThread(1, executeCount)
  waitCompleted(executeCount)
  printResult(successCount.intValue, failCount.intValue, (System.currentTimeMillis() - startTime) / 1000.0)

  System.exit(0)

  def startThread(current: Int, total: Int): Unit = {
    runClient
    if (current != total) startThread(current + 1, total)
  }

  def runClient = {
    client.onComplete {
      case Success(s) => successCount.incrementAndGet
      case Failure(f) => failCount.incrementAndGet()
    }
  }

  def waitCompleted(waitCount: Int): Unit = {
    if (waitCount == (successCount.intValue + failCount.intValue)) return
    Thread.sleep(100)
    waitCompleted(waitCount)
  }

  def printResult(succ: Int, fail: Int, time: Double) = {
    println(s"success count - $succ")
    println(s"fail count - $fail")
    println(s"total time - $time")
  }

  def client: Future[Boolean] = Future {
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
          .foreach(s => s)
        br.close()
      }
    } catch {
      case e: Exception => {
        e.printStackTrace
        throw e
      }
    } finally {
      httpClient.close()
    }
    true
  }
}
