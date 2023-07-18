import Dependencies.*
import sbt.*
import sbt.Keys.*

object BuildHelper {

  private val javaTarget = "17"

  def env(v: String): Option[String] = sys.env.get(v)

  def stdSettings(logging: Boolean = true) =
    noDoc ++ Seq(
      addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.13.2" cross CrossVersion.full),
      addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1"),
      javacOptions ++= Seq("-source", javaTarget, "-target", javaTarget),
      scalacOptions ++= Seq("-Ymacro-annotations", "-Xsource:3", s"-release:$javaTarget"),
      scalacOptions --= (if (insideCI.value) Nil else Seq("-Xfatal-warnings")), // enforced by the pre-push hook too
      // format: on
      libraryDependencies ++= Seq(Zio, prelude) ++ (if (logging) loggingLibs else Seq.empty) ++ tests.map(_ % Test),
      testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework"),
      excludeDependencies ++= Seq(
        ExclusionRule("log4j", "log4j"),
        ExclusionRule("org.slf4j", "slf4j-log4j12"),
        ExclusionRule("commons-logging", "commons-logging"),
      ),
    )

  lazy val noDoc = Seq(
    (Compile / doc / sources)                := Seq.empty,
    (Compile / packageDoc / publishArtifact) := false,
  )

  /**
   * Copied from Cats
   */
  lazy val noPublishSettings = Seq(
    publish         := {},
    publishLocal    := {},
    publishM2       := {},
    publishArtifact := false,
  )

}
