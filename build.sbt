name := "ludumdare23"

version := "1.0"

scalaVersion := "2.9.1"


resolvers += "Guicefruit Repository" at "http://guiceyfruit.googlecode.com/svn/repo/releases"

resolvers += "Sonatype Public" at "https://oss.sonatype.org/content/groups/public/"


// Inversion of control:

libraryDependencies += "com.google.inject" % "guice" % "3.0"


// Logging:

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.6.4"

libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.6.4"

libraryDependencies += "log4j" % "log4j" % "1.2.16"


// Testing:

libraryDependencies += "junit" % "junit" % "4.4"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.7.2" % "test"


// Config files:

libraryDependencies += "org.yaml" % "snakeyaml" % "1.11-SNAPSHOT"


