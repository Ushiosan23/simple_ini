import conf.LocalLoader

plugins {
	id("io.github.gradle-nexus.publish-plugin")
}

allprojects {
	repositories {
		mavenCentral()
		maven {
			url = uri("https://plugins.gradle.org/m2/")
		}
	}
}

nexusPublishing {
	repositories {
		sonatype {
			val env = LocalLoader.loadAll(rootProject)
			// Configure sonatype account
			stagingProfileId.set(env["OSSRH_PROFILE_ID"] as String)
			username.set(env["OSSRH_USERNAME"] as String)
			password.set(env["OSSRH_PASSWORD"] as String)
		}
	}
}