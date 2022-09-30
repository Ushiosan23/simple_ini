# Simple INI

[![javadoc](https://javadoc.io/badge2/com.github.ushiosan23/simple-ini/simple--ini.svg?logo=openjdk)](https://javadoc.io/doc/com.github.ushiosan23/simple-ini)

A lightweight library for loading and handling ini files and data.

Handling of basic and advanced ini files, for example ini files with attributes or multiline entries,
while these behaviors are not standard to the format itself, this behavior was added as an option and
has to be changed via a configuration object before it can be used.
We can see this type of behavior in configuration files (.conf) that have a syntax similar to .ini or the files
that Godot generates in its scenes or resources.

## How can use it

- You can download the source code and compile it.
- You can also download the precompiled jar files, but without the required libraries to make them work.
	- If you want to know what libraries you use then go [here](./DEPENDENCIES.md)
- We strongly recommend that you use a build system like maven or gradle as configuration is much easier and
  prevents headaches

### Maven configuration

If you use maven as the build system then the configuration should be as follows:

```xml

<dependencies>
	<dependency>
		<groupId>com.github.ushiosan23</groupId>
		<artifactId>simple-ini</artifactId>
		<version>x.x.x</version>
	</dependency>
</dependencies>
```

### Gradle configuration

If you are one of those who uses Groovy DSL:

```groovy
dependencies {
	implementation "com.github.ushiosan23:simple-ini:x.x.x"
}
```

If you are one of those who uses Kotlin DSL:

```kotlin
dependencies {
	implementation("com.github.ushiosan23:simple-ini:x.x.x")
}
```

### Example

A small example of how to load an ini file from java code:

```java
package my.awesone_package;

// Import all elements

import java.io.IOException;
import java.nio.file.Path;

import ushiosan.simple_ini.SimpleIni;
import ushiosan.simple_ini.IniOptions;
import ushiosan.simple_ini.section.Section;
import ushiosan.simple_ini.section.advanced.SectionAdvanced;


class Example {

	// Entry main point
	public static void main(String[] args) throws IOException {
		// Simple
		SimpleIni<Section> simple = loadSimpleIni();
		Section simpleSection = simple.getDefaultSection();

		System.out.println(simpleSection);

		// Advanced
		SimpleIni<SectionAdvanced> advanced = loadAdvancedIni();
		SectionAdvanced advancedSection = advanced.getDefaultSection();

		System.out.println(advancedSection.getAttributes());
	}

	// Simple load ini file
	private static SimpleIni<Section> loadSimpleIni() throws IOException {
		// Base variables
		Path simpleIniPath = Path.of("my_simple_ini_file.ini");
		SimpleIni<Section> ini = new SimpleIni();
		// Load ini content
		ini.load(simpleIniPath);
		return ini;
	}

	// Advanced load ini file
	private static SimpleIni<SectionAdvanced> loadAdvancedIni() throws IOException {
		// Base variables
		Path advancedIniPath = Path.of("my_advanced_ini_file.ini");
		SimpleIni<SectionAdvanced> ini = new SimpleIni();
		IniOptions config = IniOptions.createBuilder()
			.setAdvanced(true) // Optional -> default false
			.setMultiline(true) // Optional -> default false
			.build();

		// Load ini content
		ini.load(advancedIniPath, config);
		return ini;
	}

}

```
