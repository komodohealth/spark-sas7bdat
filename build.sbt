name := "spark-sas7bdat"
version := "1.1.4-SNAPSHOT"
organization := "com.github.saurfang"

scalaVersion := "2.11.6"
crossScalaVersions := Seq("2.10.5", "2.11.6")

scalacOptions ++= Seq("-target:jvm-1.7" )
javacOptions ++= Seq("-source", "1.7", "-target", "1.7")

libraryDependencies ++= Seq(
  "com.databricks" %% "spark-csv" % "1.2.0",
  "com.ggasoftware" % "parso" % "1.2.1",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)

//sbt-spark-package
spName := "saurfang/spark-sas7bdat"
sparkVersion := "1.6.1"
sparkComponents += "sql"
spAppendScalaVersion := true
credentials += Credentials(Path.userHome / ".ivy2" / ".sbtcredentials")
licenses += "GPL-3.0" -> url("http://opensource.org/licenses/GPL-3.0")

//include provided dependencies in sbt run task
run in Compile <<= Defaults.runTask(fullClasspath in Compile, mainClass in (Compile, run), runner in (Compile, run))

//only one living spark-context is allowed
parallelExecution in Test := false

//skip test during assembly
test in assembly := {}

//override modified parser class
assemblyMergeStrategy in assembly := {
  case PathList("com", "ggasoftware", xs @ _*)         => MergeStrategy.first
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

//publishTo := Some(Resolver.file("file",  new File(Path.userHome.absolutePath+"/.m2/repository")))
publishTo := Some("Local Nexus" at "http://localhost:8081/content/repositories/snapshots")

// fun fact the first parameter in Credentials MUST read EXACTLY:
// "Sonatype Nexus Repository Manager"
credentials += Credentials("Sonatype Nexus Repository Manager", "localhost", "deployment", "deployment123")
