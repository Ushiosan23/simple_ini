package ushiosan.simple_ini.conversion;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

public class NumberConverterTest {

	@Test
	public void runTest() {
		Optional<Number> resultN = NumberConverter.toNumber("12");
		Assert.assertTrue(resultN.isPresent());
		System.out.println(resultN.get());

		Optional<Byte> resultB = NumberConverter.toByte("127");
		Assert.assertTrue(resultB.isPresent());
		System.out.println(resultB.get());

		Optional<Integer> invalid = NumberConverter.toInt("ab1234");
		Assert.assertTrue(invalid.isEmpty());
		System.out.println(invalid);
	}

}