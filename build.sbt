name := "waterfall"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= {
  val akkaVersion = "2.4.0"

  Seq(
    "org.apache.httpcomponents" % "httpclient" % "4.5.1",
    "com.typesafe.akka" % "akka-actor_2.11" % akkaVersion,
    "io.netty" % "netty-all" % "4.0.32.Final",
    "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"
  )
}