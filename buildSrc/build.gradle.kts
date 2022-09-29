plugins {
	`kotlin-dsl`
}

repositories {
	mavenCentral()
	maven {
		setUrl("https://plugins.gradle.org/m2/")
	}
}

dependencies {
	implementation("io.github.gradle-nexus:publish-plugin:1.1.0")
}
