name := "fp-ecommerce/"

scalaVersion := "2.11.8"

resolvers += Resolver.sonatypeRepo("releases")

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.4")

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.2.14",
  "org.scalaz" %% "scalaz-concurrent" % "7.2.14",
  "org.scalactic" %% "scalactic" % "3.0.1",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)


