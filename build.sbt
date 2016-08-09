
name := "mockito-sweetener"

organization := "com.github.jostly"

version := "0.1.3-SNAPSHOT"

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
scalaVersion := "2.11.7"

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
  "org.scala-lang.modules" %% "scala-xml" % "1.0.2" withSources() withJavadoc(),
  "org.scala-lang" % "scala-reflect" % scalaVersion.value withSources() withJavadoc(),
  "org.scalatest" %% "scalatest" % "2.2.6" % "optional" withSources() withJavadoc(),
  "org.mockito" % "mockito-all" % "1.9.0" % "optional" withSources() withJavadoc()
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