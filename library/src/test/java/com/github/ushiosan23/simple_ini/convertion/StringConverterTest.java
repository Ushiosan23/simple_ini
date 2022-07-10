package com.github.ushiosan23.simple_ini.convertion;

import org.junit.Assert;
import org.junit.Test;

public class StringConverterTest {

	@Test
	public void runTest() {
		String content = "\"Hello,        World!.  How are    you?\"";
		String cleanContent = StringConverter.cleanStringContent(content);

		Assert.assertTrue(cleanContent.length() < content.length());

		System.out.printf("Dirty content: %s\n", content);
		System.out.printf("Clean content: %s\n", cleanContent);
	}

}