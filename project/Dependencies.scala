import sbt._

object Dependencies {
  val LogbackVersion = "1.5.4"
  lazy val Logback = "ch.qos.logback" % "logback-classic" % LogbackVersion

  val Slf4JVersion = "2.0.12"
  lazy val Slf4JApi = "org.slf4j" % "slf4j-api" % Slf4JVersion
  lazy val JclOverSlf4J = "org.slf4j" % "jcl-over-slf4j" % Slf4JVersion
  lazy val Log4JOverSlf4J = "org.slf4j" % "log4j-over-slf4j" % Slf4JVersion
  lazy val JulToSlf4J = "org.slf4j" % "jul-to-slf4j" % Slf4JVersion

  val ZioHttpVersion = "3.0.0-RC6"
  lazy val ZioHttp = "dev.zio" %% "zio-http" % ZioHttpVersion

  val ZioVersion = "2.0.21"
  lazy val Zio = "dev.zio" %% "zio" % ZioVersion
  lazy val ZioTest = "dev.zio" %% "zio-test" % ZioVersion
  lazy val ZioTestMagnolia = "dev.zio" %% "zio-test-magnolia" % ZioVersion
  lazy val ZioTestSbt = "dev.zio" %% "zio-test-sbt" % ZioVersion
}
