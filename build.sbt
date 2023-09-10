import Dependencies._

val Scala2Version = "2.13.11"
val Scala3Version = "3.3.1"

ThisBuild / scalaVersion := Scala2Version // For JDK 16 compatibility

ThisBuild / organization := "com.gregorpurdy.ident"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / versionScheme := Some("early-semver")
ThisBuild / organizationName := "Gregor Purdy"
ThisBuild / organizationHomepage := Some(url("https://github.com/gnp/zio-quickstart-https"))
ThisBuild / description := "ISIN."
ThisBuild / startYear := Some(2023)
ThisBuild / licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"))
ThisBuild / homepage := Some(url("https://github.com/gnp/isin-sc"))
ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/gnp/isin-sc"),
    "scm:git@github.com:gnp/zio-quickstart-https.git"
  )
)
ThisBuild / developers := List(
  Developer(
    "gnp",
    "Gregor Purdy",
    "gregor@abcelo.com",
    url("http://github.com/gnp")
  )
)

ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision

addCommandAlias(
  "check",
  "; headerCheck; scalafmtSbtCheck; scalafmtCheckAll; scalafixAll --check"
)

val stdCompilerOptions2 = Seq(
  "-deprecation", // Emit warning and location for usages of deprecated APIs.
  "-encoding",
  "utf-8", // Specify character encoding used by source files.
  "-feature", // Emit warning and location for usages of features that should be imported explicitly.
  "-unchecked", // Enable additional warnings where generated code depends on assumptions.
  "-Wdead-code", // Warn when dead code is identified.
  "-Wextra-implicit", // Warn when more than one implicit parameter section is defined.
  "-Wmacros:before", // Enable lint warnings on macro expansions. Default: `before`, `help` to list choices.
  "-Wnumeric-widen", // Warn when numerics are widened.
  "-Woctal-literal", // Warn on obsolete octal syntax.
  "-Wunused:explicits", // Warn if an explicit parameter is unused.
//  "-Wunused:implicits", // Warn if an implicit parameter is unused.
  "-Wunused:imports", // Warn if an import selector is not referenced.
  "-Wunused:linted", // -Xlint:unused.
  "-Wunused:locals", // Warn if a local definition is unused.
  "-Wunused:params", // Enable -Wunused:explicits,implicits.
  "-Wunused:patvars", // Warn if a variable bound in a pattern is unused.
  "-Wunused:privates", // Warn if a private member is unused.
  "-Wvalue-discard", // Warn when non-Unit expression results are unused.
  "-Xlint:adapted-args", // Warn if an argument list is modified to match the receiver.
  "-Xlint:constant", // Evaluation of a constant arithmetic expression results in an error.
  "-Xlint:delayedinit-select", // Selecting member of DelayedInit.
  "-Xlint:deprecation", // Enable linted deprecations.
  "-Xlint:doc-detached", // A Scaladoc comment appears to be detached from its element.
  "-Xlint:eta-sam", // Warn on eta-expansion to meet a Java-defined functional interface that is not explicitly annotated with @FunctionalInterface.
  "-Xlint:eta-zero", // Warn on eta-expansion (rather than auto-application) of zero-ary method.
  "-Xlint:implicit-not-found", // Check @implicitNotFound and @implicitAmbiguous messages.
  "-Xlint:inaccessible", // Warn about inaccessible types in method signatures.
  "-Xlint:-infer-any", // DISABLED (because ZIO) Warn when a type argument is inferred to be `Any`.
  "-Xlint:missing-interpolator", // A string literal appears to be missing an interpolator id.
  "-Xlint:nonlocal-return", // A return statement used an exception for flow control.
  "-Xlint:nullary-unit", // Warn when nullary methods return Unit.
  "-Xlint:option-implicit", // Option.apply used implicit view.
  "-Xlint:package-object-classes", // Class or object defined in package object.
  "-Xlint:poly-implicit-overload", // Parameterized overloaded implicit methods are not visible as view bounds.
  "-Xlint:private-shadow", // A private field (or class parameter) shadows a superclass field.
  "-Xlint:serial", // @SerialVersionUID on traits and non-serializable classes.
  "-Xlint:stars-align", // Pattern sequence wildcard must align with sequence component.
  "-Xlint:type-parameter-shadow", // A local type parameter shadows a type already in scope.
  "-Xlint:unused", // Enable -Ywarn-unused:imports,privates,locals,implicits.
  "-Xlint:valpattern", // Enable pattern checks in val definitions.
  "-Xsource:3",
  "-Xfatal-warnings"
)

val stdCompilerOptions3 = Seq(
  "-deprecation", // Emit warning and location for usages of deprecated APIs.
  "-encoding",
  "utf-8", // Specify character encoding used by source files.
  "-feature", // Emit warning and location for usages of features that should be imported explicitly.
  "-unchecked", // Enable additional warnings where generated code depends on assumptions.
  "-Wunused:explicits", // Warn if an explicit parameter is unused.
//  "-Wunused:implicits", // Warn if an implicit parameter is unused.
  "-Wunused:imports", // Warn if an import selector is not referenced.
  "-Wunused:linted", // -Xlint:unused.
  "-Wunused:locals", // Warn if a local definition is unused.
  "-Wunused:params", // Enable -Wunused:explicits,implicits.
  "-Wunused:privates", // Warn if a private member is unused.
  "-Wvalue-discard", // Warn when non-Unit expression results are unused.
  "-Xfatal-warnings"
)

lazy val root = (project in file("."))
  .aggregate(
    api
  )
  .settings(
    name := "root",
    crossScalaVersions := Nil, // To avoid "double publishing"
    publish := {},
    publish / skip := true,
    publishLocal := {}
  )

lazy val api = (project in file("api"))
  .settings(
    name := "api",
    crossScalaVersions := Seq(Scala2Version, Scala3Version),
    scalacOptions ++= {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, n)) => stdCompilerOptions2
        case _            => stdCompilerOptions3
      }
    },
    libraryDependencies ++= Seq(
      Zio % Compile,
      ZioHttp % Compile,
      "org.bouncycastle" % "bcpkix-jdk18on" % "1.76" % Compile,
      Slf4JApi % Compile,
      JclOverSlf4J % Compile,
      Log4JOverSlf4J % Compile,
      JulToSlf4J % Compile,
      Logback % Compile,
      ZioTest % Test,
      ZioTestMagnolia % Test,
      ZioTestSbt % Test
    )
      .map(_.exclude("commons-logging", "commons-logging"))
      .map(_.exclude("log4j", "log4j"))
      .map(_.exclude("org.slf4j", "slf4j-log4j12")),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework"),
    mainClass := Some("com.gregorpurdy.MainApp")
  )

run / fork := true
run / javaOptions += "-Dtrust_all_cert=true"
