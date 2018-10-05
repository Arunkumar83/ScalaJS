name := "apm-frontend"
organization := "com.arun"
version := "1.0"
scalaVersion := "2.12.3"

enablePlugins(ScalaJSPlugin)

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

//set scalaJSOptimizerOptions ~= { _.withBypassLinkingErrors(true) }

libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.6"

libraryDependencies += "org.querki" %%% "jquery-facade" % "1.2"

libraryDependencies += "com.lihaoyi" %%% "upickle" % "0.6.6"

libraryDependencies += "com.lihaoyi" %%% "scalatags" % "0.6.7"

// This is an application with a main method
scalaJSUseMainModuleInitializer := true

//Revolver.settings