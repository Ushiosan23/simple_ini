package com.github.ushiosan23.simple_ini;

import com.github.ushiosan23.jvm.system.Rand;
import com.github.ushiosan23.simple_ini.section.Section;
import com.github.ushiosan23.simple_ini.section.advanced.SectionAdvanced;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Map;

public class SimpleIniTest {

	private final ClassLoader loader = ClassLoader.getSystemClassLoader();

	@Test
	public void simpleLoadRunTest() throws IOException {
		InputStream stream = loader.getResourceAsStream("simple_example.ini");
		Assert.assertNotNull(stream);

		SimpleIni<Section> ini = new SimpleIni<>();
		ini.load(stream);

		Assert.assertFalse(ini.isEmpty());
		for (Section section : ini.getSections()) {
			System.out.printf("[%s]\n", section.getName());

			for (Map.Entry<String, String> entry : section.entrySet()) {
				System.out.printf("\t%s\n", entry);
			}
		}

		System.out.println();
	}

	@Test
	public void advancedLoadRunTest() throws IOException {
		InputStream stream = loader.getResourceAsStream("advanced_example.ini");
		Assert.assertNotNull(stream);

		SimpleIni<SectionAdvanced> ini = new SimpleIni<>();
		IniOptions options = IniOptions
			.createBuilder()
			.setAdvanced(true)
			.setMultiline(true)
			.build();
		ini.load(stream, options);

		Assert.assertFalse(ini.isEmpty());
		for (SectionAdvanced section : ini.getSections()) {
			System.out.printf("[%s %s]\n", section.getName(), section.getAttributes().entrySet());

			for (Map.Entry<String, String> entry : section.entrySet()) {
				System.out.printf("\t%s\n", entry);
			}
		}

		System.out.println();
	}

	@Test
	public void storeIniTest() throws IOException, URISyntaxException {
		URL location = loader.getResource("stored_example.ini");
		Assert.assertNotNull(location);
		InputStream stream = location.openStream();

		SimpleIni<SectionAdvanced> ini = new SimpleIni<>();
		IniOptions options = IniOptions
			.createBuilder()
//			.setAdvanced(true)
//			.setMultiline(true)
			.build();
		ini.load(stream, options);

		// Generate random element
		for (var section : ini.getSections()) {
			for (int i = 0; i < 10; i++) {
				String randomKey = Rand.getRandomString(
					Rand.getSystemRandom().nextInt(15),
					Rand.RAND_LETTERS);
				String randomValue = Rand.getRandomString(Rand.getSystemRandom().nextInt(50));
				// Insert content
				section.put(randomKey, randomValue);
			}
		}

		// Generate writer
		Path filePath = Path.of(location.toURI());

		System.err.println(filePath);

		ini.store(filePath);
		System.out.println();
	}

}