name := "bc4s"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.12.7"

val akkaVersion = "2.5.13"
val scalaTestVersion = "3.0.5"

resolvers += Resolver.mavenLocal

libraryDependencies ++= Seq(
  "commons-codec" % "commons-codec" % "1.15",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion  % Test,
  "org.scalatest" %% "scalatest" % scalaTestVersion  % Test
)
