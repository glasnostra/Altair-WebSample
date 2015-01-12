import java.io.FileInputStream
import java.nio.file.Paths
import java.util.Properties

import com.earldouglas.xsbtwebplugin.PluginKeys._
import com.earldouglas.xsbtwebplugin.WebPlugin._
import org.flywaydb.sbt.FlywayPlugin._
import org.scalatra.sbt._
import sbt.Keys._
import sbt._
import xerial.sbt.Pack._

object Build extends Build {

  lazy val Organization = "org.altairtoolkit"
  lazy val Name = "Altair-WebSample"
  lazy val Version = "1.0"
  lazy val ScalaVersion = "2.11.4"
  lazy val ScalatraVersion = "2.3.0"
  lazy val SpringVersion = "4.1.3.RELEASE"
  lazy val SpringSecurityVersion = "3.2.5.RELEASE"
  lazy val DbcpVersion = "6.0.41"
  lazy val ScalaLoggingVersion = "3.1.0"
  lazy val SlickVersion = "2.1.0"
  lazy val AkkaVersion = "2.3.6"
  lazy val LogbackVersion = "1.1.2"
  lazy val Json4sVersion = "3.2.11"
  lazy val HttpClientVersion = "4.3.6"
  lazy val ScalaAsyncModule = "0.9.2"
  lazy val TypesafeConfigVersion = "1.2.1"
  lazy val HazelcastVersion = "3.3.2"
  lazy val ImgScalrVersion = "4.2"
  lazy val SlugifyVersion = "2.1.2"
  lazy val QuartzVersion = "2.2.+"
  lazy val LogstashEncoderVersion = "3.4"
  lazy val ScalaTestVersion = "2.2.3"
  lazy val JettyVersion = "9.1.5.v20140505"

  lazy val jdbcProperties = loadProperty("src/main/resources/jdbc.properties")

  def loadProperty(path: String): String => String = {
    lazy val propertyFile = Paths.get(path).toFile
    lazy val jdbcProp: Properties = new Properties()
    jdbcProp.load(new FileInputStream(propertyFile))
    (name) => {
      jdbcProp.getProperty(name)
    }
  }

  lazy val project = Project(
    Name,
    file("."),
    settings = ScalatraPlugin.scalatraWithDist ++ packSettings ++ Seq(
      scalacOptions ++= Seq("-unchecked", "-deprecation"),
      packJvmOpts := Map("runner" -> Seq("-Xmx1024m")),
      packMain := Map("runner" -> "org.altairtoolkit.sample.web.main.Runner"),
      port in container.Configuration := 8787,
      organization := Organization,
      conflictManager := ConflictManager.latestRevision,
      name := Name,
      version := Version,
      scalaVersion := ScalaVersion,
      resolvers += Classpaths.typesafeReleases,
      resolvers += "Jasoet Nexus" at "http://104.207.150.166:8081/nexus/content/groups/public/",
      libraryDependencies ++= (libraryDependencies in ThisBuild).value ++ Seq(
        "org.scalatra" %% "scalatra" % ScalatraVersion,
        "org.scalatra" %% "scalatra-auth" % ScalatraVersion,
        "com.typesafe.slick" %% "slick" % SlickVersion,
        "com.typesafe.slick" %% "slick-codegen" % SlickVersion,
        "org.scalatest" %% "scalatest" % ScalaTestVersion % "test",
        "ch.qos.logback" % "logback-classic" % LogbackVersion,
        "org.springframework" % "spring-core" % SpringVersion force(),
        "org.springframework" % "spring-web" % SpringVersion force(),
        "org.springframework" % "spring-context-support" % SpringVersion force(),
        "org.springframework.security" % "spring-security-web" % SpringSecurityVersion,
        "org.springframework.security" % "spring-security-config" % SpringSecurityVersion,
        "org.springframework.security" % "spring-security-core" % SpringSecurityVersion,
        "org.apache.tomcat" % "dbcp" % DbcpVersion,
        "javax.servlet" % "javax.servlet-api" % "3.1.0",
        "com.typesafe.scala-logging" %% "scala-logging" % ScalaLoggingVersion,
        "org.scalatra" %% "scalatra-json" % ScalatraVersion,
        "org.json4s" %% "json4s-jackson" % Json4sVersion,
        "com.typesafe.akka" %% "akka-actor" % AkkaVersion,
        "com.typesafe.akka" %% "akka-actor" % AkkaVersion,
        "org.apache.httpcomponents" % "httpclient" % HttpClientVersion,
        "org.scala-lang.modules" %% "scala-async" % ScalaAsyncModule,
        "org.imgscalr" % "imgscalr-lib" % ImgScalrVersion,
        "com.github.slugify" % "slugify" % SlugifyVersion,
        "org.quartz-scheduler" % "quartz" % QuartzVersion,
        "org.eclipse.jetty" % "jetty-webapp" % JettyVersion % "container",
        "org.eclipse.jetty" % "jetty-plus" % JettyVersion % "container",
        "org.eclipse.jetty" % "jetty-webapp" % JettyVersion,
        "org.skinny-framework" % "skinny-validator_2.11" % "1.3.6",
        "org.postgresql" % "postgresql" % "9.3-1102-jdbc41",
        "net.logstash.logback" % "logstash-logback-encoder" % LogstashEncoderVersion,
        "org.altairtoolkit" %% "altair-toolkit" % "1.0.RC6"
      ),
      resourceGenerators in Compile <+= (resourceManaged, baseDirectory) map { (managedBase, base) =>
        val webappBase = base / "src" / "main" / "webapp"
        for {
          (from, to) <- webappBase ** "*" pair rebase(webappBase, managedBase / "main" / "webapp")
        } yield {
          Sync.copy(from, to)
          to
        }
      }
    )
  ).enablePlugins(play.twirl.sbt.SbtTwirl)
    .settings(flywaySettings: _*).settings(
      flywayUrl := jdbcProperties("jdbc.url"),
      flywayUser := jdbcProperties("jdbc.username"),
      flywayPassword := jdbcProperties("jdbc.password"),
      flywayDriver := jdbcProperties("jdbc.driver"),
      flywayLocations := Seq("migration"),
      flywayTable := "SCHEMA_VERSION"
    )
}
