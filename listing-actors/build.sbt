name := "listing-actors"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.12.7"

val akkaVersion = "2.5.13"
val scalaTestVersion = "3.0.5"

resolvers += Resolver.mavenLocal

libraryDependencies ++= Seq(
  "me.devtools4.telegram" % "alphavantage-rest" % "0.0.1-SNAPSHOT",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion  % Test,
  "org.scalatest" %% "scalatest" % scalaTestVersion  % Test
)
