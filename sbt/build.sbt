name := "Smack Playground for Scala"

version := "1.0"

libraryDependencies += "org.igniterealtime.smack" % "smack-tcp" % "4.1.0-beta1"
libraryDependencies += "org.igniterealtime.smack" % "smack-java7" % "4.1.0-beta1"
libraryDependencies += "org.igniterealtime.smack" % "smack-extensions" % "4.1.0-beta1"
libraryDependencies += "org.igniterealtime.smack" % "smack-experimental" % "4.1.0-beta1"

initialCommands in console += "import org.jivesoftware.smack.tcp._;"
initialCommands in console += "import org.jivesoftware.smack._;"
initialCommands in console += "SmackConfiguration.DEBUG_ENABLED = true;"
initialCommands in console += "SmackConfiguration.addDisabledSmackClass(\"org.jivesoftware.smack.debugger.JulDebugger\");"
