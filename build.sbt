
name := "mockito-sweetener"

organization := "com.github.jostly"

version := "0.2.0"

homepage := Some(url("https://github.com/jostly/mockito-sweetener"))

startYear := Some(2016)

licenses := Seq(
  ("Unlicense", url("http://unlicense.org"))
)

//bintrayReleaseOnPublish in ThisBuild := false

scmInfo := Some(
  ScmInfo(
    url("https://github.com/jostly/mockito-sweetener"),
    "scm:git:https://github.com/jostly/mockito-sweetener.git",
    Some("scm:git:git@github.com:jostly/mockito-sweetener.git")
  )
)

bintrayVcsUrl := Some("scm:git:git@github.com:jostly/mockito-sweetener.git")

/* scala versions and options */
scalaVersion := "2.11.8"

scalacOptions ++= Seq(
  "-deprecation",
  "-unchecked",
  "-encoding", "UTF-8"
  // "-Xcheckinit" // for debugging only, see https://github.com/paulp/scala-faq/wiki/Initialization-Order
  // "-optimise"   // this option will slow your build
)

scalacOptions ++= Seq(
  "-Yclosure-elim",
  "-Yinline"
)

libraryDependencies ++= Seq (
  "org.scala-lang.modules" %% "scala-xml" % "1.0.5",
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  "org.scalatest" %% "scalatest" % "3.0.0" % "optional",
  "org.mockito" % "mockito-all" % "1.10.19" % "optional"
)

/* sbt behavior */
logLevel in compile := Level.Warn

traceLevel := 5

offline := false

/* publishing */
publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := <developers>
  <developer>
    <id>jostly</id>
    <name>Johan Östling</name>
    <url>https://github.com/jostly</url>
  </developer>
</developers>

initialCommands := "import com.github.jostly.scalatest.mock.MockitoSweetener._"