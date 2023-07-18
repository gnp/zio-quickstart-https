import Dependencies._
import BuildHelper._

Global / onChangedBuildSource := ReloadOnSourceChanges

val Scala2Version = "2.13.11"
val Scala3Version = "3.3.0"

ThisBuild / organization := "com.gregorpurdy.ident"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := Scala2Version
ThisBuild / crossScalaVersions := Seq(Scala2Version, Scala3Version)
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
    "scm:git@github.com:gnp/zio-quickstart-https.git",
  )
)
ThisBuild / developers := List(
  Developer(
    "gnp",
    "Gregor Purdy",
    "gregor@abcelo.com",
    url("http://github.com/gnp"),
  )
)

ThisBuild / scalafmtCheck := true
ThisBuild / scalafmtSbtCheck := true
ThisBuild / scalafmtOnCompile := !insideCI.value
ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbOptions += "-P:semanticdb:synthetics:on"
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision // use Scalafix compatible version
ThisBuild / scalafixScalaBinaryVersion := CrossVersion.binaryScalaVersion(scalaVersion.value)
ThisBuild / scalafixDependencies ++= List(
  "com.github.vovapolu" %% "scaluzzi" % "0.1.23",
  "io.github.ghostbuster91.scalafix-unified" %% "unified" % "0.0.9",
)

addCommandAlias(
  "check",
  "; headerCheck; scalafmtSbtCheck; scalafmtCheckAll; scalafixAll --check",
)
addCommandAlias("tc", "Test/compile")
addCommandAlias("ctc", "clean; Test/compile")
addCommandAlias("rctc", "reload; clean; Test/compile")
addCommandAlias("start", "~api/reStart")
addCommandAlias("stop", "reStop")
addCommandAlias("generateCli", "cli/nativeImage")
addCommandAlias("cli-say-hi", """cli/run get-hello""") // You need to `start` the API first in another sbt instance
// Will generate the native image config files thanks to the GraalVM "Tracing Agent"
// You need to `start` the API first in another sbt instance
addCommandAlias("generateCliNativeConfig", "cli/nativeImageRunAgent \" get-hello\"")
// Will compile the CLI to a native executable binary
addCommandAlias("compileCliNativeBinary", "cli/nativeImage")
// Both generated the CLI native configs and compiles the CLI to a native executable binary
addCommandAlias("generateCliNativeConfigAndBinary", "generateCliNativeConfig;compileCliNativeBinary")

lazy val root =
  Project(id = "zio-quickstart-https", base = file("."))
    .disablePlugins(RevolverPlugin)
    .settings(noDoc *)
    .settings(noPublishSettings *)
    .aggregate(
      cli,
      `cli-api`,
      api,
    )

lazy val cli =
  (project in file("modules/cli"))
    .disablePlugins(RevolverPlugin)
    .enablePlugins(BuildInfoPlugin, NativeImagePlugin)
    .settings(stdSettings(logging = false): _*)
    .settings(
      libraryDependencies ++= Seq(ZioHttpCli),
      Compile / mainClass := Some("com.gregorpurdy.cli.MyCLI"),
    )
    .settings(
      // BuildInfo settings
      buildInfoKeys := Seq[BuildInfoKey](version),
      buildInfoPackage := "com.gregorpurdy.cli",
      buildInfoObject := "CliBuildInfo",
    )
    .settings(
      // sbt-native-image configs
      nativeImageVersion := "17.0.7",
      nativeImageJvm := "graalvm",
      nativeImageOptions ++= {
        Seq(
          "--no-fallback",
          "--enable-http",
          "--enable-https",
          "--enable-url-protocols=http,https",
          // "--enable-sbom", // only available on Oracle GraalVM
          "--install-exit-handlers",
          "--diagnostics-mode",
          // "-H:+BuildReport", // only available on Oracle GraalVM
          "-H:ExcludeResources=.*.jar,.*.properties",
          "-Djdk.http.auth.tunneling.disabledSchemes=",
          "--initialize-at-run-time=io.netty.handler.ssl.BouncyCastleAlpnSslUtils",
        )
      },
      nativeImageAgentMerge := true,
      nativeImageOptions += s"-H:ConfigurationFileDirectories=${(Compile / resourceDirectory).value}/META-INF/native-image",
      nativeImageAgentOutputDir := (Compile / resourceDirectory).value / "META-INF/native-image",
      nativeImageOutput := (ThisBuild / baseDirectory).value / "my-cli", // The generated executable binary
      Global / excludeLintKeys ++= Set(nativeImageVersion, nativeImageJvm), // Wrongly reported as unused keys by sbt
    )
    .dependsOn(`cli-api`)

lazy val `cli-api` =
  (project in file("modules/cli-api"))
    .disablePlugins(RevolverPlugin)
    .settings(stdSettings(logging = false): _*)
    .settings(
      libraryDependencies ++= Seq(ZioHttp)
    )

lazy val api =
  (project in file("modules/api"))
    .settings(stdSettings(): _*)
    .settings(
      Compile / mainClass := Some("com.gregorpurdy.api.MainApp"),
      libraryDependencies ++= Seq(
        ZioHttp,
        "org.bouncycastle" % "bcpkix-jdk18on" % "1.72",
      ),
      run / fork := true,
      run / javaOptions += "-Dtrust_all_cert=true"
    )
    .dependsOn(`cli-api`)
