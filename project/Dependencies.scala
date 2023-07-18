import sbt.*

object Dependencies {
  val LogbackVersion = "1.4.8"
  val slf4jVersion   = "2.0.7"
  val Slf4JVersion   = "2.0.7"
  val ZioHttpVersion = "3.0.0-RC2"
  val ZioVersion     = "2.0.15"

  lazy val Zio        = "dev.zio" %% "zio"          % ZioVersion
  lazy val ZioHttp    = "dev.zio" %% "zio-http"     % ZioHttpVersion
  lazy val ZioHttpCli = "dev.zio" %% "zio-http-cli" % ZioHttpVersion
  lazy val prelude    = "dev.zio" %% "zio-prelude"  % "1.0.0-RC19"

  val tests = Seq(
    "dev.zio" %% "zio-test"          % ZioVersion,
    "dev.zio" %% "zio-test-sbt"      % ZioVersion,
    "dev.zio" %% "zio-test-magnolia" % ZioVersion,
    "dev.zio" %% "zio-mock"          % "1.0.0-RC11",
  )

  val loggingLibs = Seq(
    "dev.zio"             %% "zio-logging-slf4j2"       % "2.1.13",
    "ch.qos.logback"       % "logback-classic"          % "1.4.8",
    "net.logstash.logback" % "logstash-logback-encoder" % "7.4",
    "org.slf4j"            % "jul-to-slf4j"             % slf4jVersion,
    "org.slf4j"            % "log4j-over-slf4j"         % slf4jVersion,
    "org.slf4j"            % "jcl-over-slf4j"           % slf4jVersion,
    "org.slf4j"            % "slf4j-api"                % slf4jVersion,
  )

}
