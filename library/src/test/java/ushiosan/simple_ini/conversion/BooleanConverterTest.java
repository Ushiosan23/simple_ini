package ushiosan.simple_ini.conversion;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

import ushiosan.jvm_utilities.lang.collection.Arrs;

public class BooleanConverterTest {

	@Test
	public void runTest() {
		CharSequence[] allValid = Arrs.of("true", "false", "1", "0", "yes", "no", "y", "n");

		for (CharSequence valid : allValid) {
			Optional<Boolean> result = BooleanConverter.toBoolean(valid);

			Assert.assertTrue(result.isPresent());
			System.out.printf("Result of %s: %s\n", valid, result);
		}

		System.out.println();

		CharSequence[] invalidCases = Arrs.of("123", "asada", "true234", "false1783");
		for (CharSequence invalid : invalidCases) {
			Optional<Boolean> result = BooleanConverter.toBoolean(invalid);

			Assert.assertTrue(result.isEmpty());
			System.out.printf("Invalid result of %s: %s\n", invalid, result);
		}
	}

}