import conf.Configs
import conf.Versions
import gradle.kotlin.dsl.accessors._64acc05bf1a66f2c855e386526b4bcff.testImplementation

plugins {
	java
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