package com.sultudy.waterfall.spike.akka.client

import akka.actor.ActorSystem

object StreamClientRunner extends App {

  val system = ActorSystem("stream-client-world")

  for(i <- 0 to 10)
    system.actorOf(StreamClient.props) ! StreamClient.Start
}
