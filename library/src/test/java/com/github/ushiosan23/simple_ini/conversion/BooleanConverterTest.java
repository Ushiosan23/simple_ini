package com.github.ushiosan23.simple_ini.conversion;

import com.github.ushiosan23.jvm.collections.Arr;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

public class BooleanConverterTest {

	@Test
	public void runTest() {
		CharSequence[] allValid = Arr.of("true", "false", "1", "0", "yes", "no", "y", "n");

		for (CharSequence valid : allValid) {
			Optional<Boolean> result = BooleanConverter.toBoolean(valid);

			Assert.assertTrue(result.isPresent());
			System.out.printf("Result of %s: %s\n", valid, result);
		}

		System.out.println();

		CharSequence[] invalidCases = Arr.of("123", "asada", "true234", "false1783");
		for (CharSequence invalid : invalidCases) {
			Optional<Boolean> result = BooleanConverter.toBoolean(invalid);

			Assert.assertTrue(result.isEmpty());
			System.out.printf("Invalid result of %s: %s\n", invalid, result);
		}
	}

}