package com.github.ushiosan23.simple_ini.convertion;

import com.github.ushiosan23.jvm.collections.Arr;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class CollectionConverterTest {

	@Test
	public void runListTest() {
		String separator = ",";
		String[] elements = Arr.of("2", "4", "6", "8", "10");

		String content = String.join(separator, elements);
		List<String> conversion = CollectionConverter.toList(content, separator);
		Assert.assertFalse(conversion.isEmpty());

		System.out.printf("Real one with (%s) separator: %s\n", separator, content);
		System.out.printf("Set result: (%s) %s\n", conversion.getClass().getCanonicalName(), conversion);
		System.out.printf("List result size: %s\n\n", conversion.size());
	}

	@Test
	public void runSetTest() {
		String separator = ",";
		String[] elements = Arr.of("2", "4", "6", "8", "10");

		String content = String.join(separator, elements);
		Set<String> conversion = CollectionConverter.toSet(content, separator);
		Assert.assertFalse(conversion.isEmpty());

		System.out.printf("Real one with (%s) separator: %s\n", separator, content);
		System.out.printf("Set result: (%s) %s\n", conversion.getClass().getCanonicalName(), conversion);
		System.out.printf("Set result size: %s\n\n", conversion.size());
	}


}