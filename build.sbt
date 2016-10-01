lazy val `mockito-sweetener` = (project in file(".")).
  enablePlugins(GitVersioning, GitBranchPrompt).
  settings(
    name := "mockito-sweetener",
    organization := "com.github.jostly",
    scalaVersion := "2.11.8",

    libraryDependencies ++= Seq(
      "org.scala-lang.modules" %% "scala-xml" % "1.0.5",
      "org.scala-lang" % "scala-reflect" % scalaVersion.value,
      "org.scalatest" %% "scalatest" % "3.0.0" % "optional",
      "org.mockito" % "mockito-all" % "1.10.19" % "optional"
    ),

    scalacOptions ++= Seq(
      "-Xfatal-warnings",
      "-Xlint:missing-interpolator",
      "-Ywarn-unused-import",
      "-Ywarn-unused",
      "-Yclosure-elim",
      "-Yinline",
      "-deprecation",
      "-feature",
      "-unchecked",
      "-explaintypes",
      "-encoding", "UTF-8"
    )
  ).
  settings(publishSettings: _*)

lazy val githubRepo = "jostly/mockito-sweetener"

lazy val publishSettings = Seq(
  homepage := Some(url(s"https://github.com/$githubRepo")),
  startYear := Some(2016),
  licenses := Seq(("Unlicense", url("http://unlicense.org"))),
  scmInfo := Some(
    ScmInfo(
      url(s"https://github.com/$githubRepo"),
      s"scm:git:https://github.com/$githubRepo.git",
      Some(s"scm:git:git@github.com:$githubRepo.git")
    )
  ),
  bintrayVcsUrl := Some(s"scm:git:git@github.com:$githubRepo.git"),
  bintrayCredentialsFile := file(".credentials"),
  publishMavenStyle := true,
  publishArtifact in Test := false,
  pomIncludeRepository := { _ => false },
  pomExtra := <developers>
    <developer>
      <id>jostly</id>
      <name>Johan Östling</name>
      <url>https://github.com/jostly</url>
    </developer>
  </developers>
)

initialCommands := "import com.github.jostly.scalatest.mock.MockitoSweetener._"
