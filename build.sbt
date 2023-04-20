ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "Hello World"
  )

libraryDependencies ++= Seq(

  "com.typesafe" % "config" % "1.4.2",
  "org.scalatest" %% "scalatest" % "3.2.1",
"org.scalatest" %% "scalatest" % "3.2.1" % "test"
)

testOptions in Test := Nil
testFrameworks += new TestFramework("org.scalatest.tools.Framework")



