name := "Smack Playground for Scala"

version := "1.0"

resolvers += Resolver.sonatypeRepo("snapshots")
resolvers += Resolver.mavenLocal

libraryDependencies += "org.igniterealtime.smack" % "smack-tcp" % "4.1.0-beta2-SNAPSHOT"
libraryDependencies += "org.igniterealtime.smack" % "smack-java7" % "4.1.0-beta2-SNAPSHOT"
libraryDependencies += "org.igniterealtime.smack" % "smack-extensions" % "4.1.0-beta2-SNAPSHOT"
libraryDependencies += "org.igniterealtime.smack" % "smack-experimental" % "4.1.0-beta2-SNAPSHOT"

initialCommands in console += "import org.jivesoftware.smack.tcp._;"
initialCommands in console += "import org.jivesoftware.smack._;"
initialCommands in console += "SmackConfiguration.DEBUG = true;"
