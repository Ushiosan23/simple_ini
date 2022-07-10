package conf

import org.gradle.api.JavaVersion

object Configs {

	object Java {
		@JvmStatic
		val sourceCompatibility = JavaVersion.VERSION_11

		@JvmStatic
		val targetCompatibility = JavaVersion.VERSION_11
	}

	object Javadoc {

		@JvmStatic
		val javaApiUrl = "https://docs.oracle.com/en/java/javase/11/docs/api/"

		@JvmStatic
		val jbAnnotationsApiUrl = "https://javadoc.io/doc/org.jetbrains/annotations/latest/"

		@JvmStatic
		val jvmUtilitiesApiUrl = "https://javadoc.io/doc/com.github.ushiosan23/jvm_utilities/latest/"

	}

}