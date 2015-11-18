lazy val commonSettings = Seq(
  organization := "com.example",
  version := "0.1.0",
  scalaVersion := "2.10.4"
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "TweetProcessing",
    libraryDependencies += "org.apache.spark" %% "spark-core" % "1.5.2",
    scalariformSettings
  )