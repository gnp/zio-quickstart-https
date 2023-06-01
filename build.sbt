scalaVersion := "2.13.10"
organization := "com.gregorpurdy"

libraryDependencies ++= Seq(
  "dev.zio" %% "zio" % "2.0.14",
  "dev.zio" %% "zio-http" % "3.0.0-RC2",
  "org.bouncycastle" % "bcpkix-jdk15on" % "1.69" % Compile
)

run / fork := true
run / javaOptions += "-Dtrust_all_cert=true"
