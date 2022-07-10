import conf.Configs
import conf.Versions

plugins {
	`java-library`
}

java {
	sourceCompatibility = Configs.Java.sourceCompatibility
	targetCompatibility = Configs.Java.targetCompatibility
}

dependencies {
	implementation("com.github.ushiosan23:jvm_utilities:${Versions.JVM_UTILITIES}")
	compileOnly("org.jetbrains:annotations:${Versions.JB_ANNOTATIONS}")
	// Test implementation
	testImplementation("junit:junit:${Versions.JUNIT}")
}