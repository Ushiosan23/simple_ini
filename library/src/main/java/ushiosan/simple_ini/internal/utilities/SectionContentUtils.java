package ushiosan.simple_ini.internal.utilities;

import org.intellij.lang.annotations.RegExp;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ushiosan.jvm_utilities.lang.collection.Collections;
import ushiosan.jvm_utilities.lang.collection.elements.Pair;
import ushiosan.simple_ini.conversion.StringConverter;
import ushiosan.simple_ini.section.data.SectionInfoTmp;

/**
 * Utilities class for handling section content
 */
public final class SectionContentUtils {

	/**
	 * This class cannot be instantiated.
	 * <p>
	 * Singleton or utility class mode.
	 */
	private SectionContentUtils() {
	}

	/* -----------------------------------------------------
	 * Properties
	 * ----------------------------------------------------- */

	/**
	 * Regular expression used to manage section attributes
	 */
	@RegExp
	static final String ATTRIBUTE_REGEX = "(\\w+)=(\"(.*?)\"|(\\d+\\.?\\d*))";

	/**
	 * Regular expression used to detect an entry element
	 */
	@RegExp
	static final String FULL_ENTRY_REGEX = "^([A-Za-z_][\\w/]+)\\s?=\\s?(.*+)$";

	/**
	 * Cached entry pattern
	 *
	 * @see #FULL_ENTRY_REGEX
	 */
	static final Pattern ENTRY_PATTERN = Pattern.compile(FULL_ENTRY_REGEX);

	/* -----------------------------------------------------
	 * Methods
	 * ----------------------------------------------------- */

	/**
	 * Returns the information of the selected section.
	 *
	 * @param content the content to check
	 * @return a section object info
	 */
	public static @NotNull SectionInfoTmp getSectionInfo(final @NotNull String content) {
		// Clean content
		final String result = StringConverter.cleanMultipleSpaceContent(
			content
				.substring(1, content.length() - 1)
				.trim()
		);
		// Get results
		String sectionName = getSectionName(result);
		Map<String, String> sectionAttributes = getSectionAttributes(result);
		// Returns the result
		return new SectionInfoTmp(sectionName, sectionAttributes);
	}

	/**
	 * Inspect the content and return the entry information if it is valid.
	 *
	 * @param content the content to inspect
	 * @return an entry info content or {@code null} if the content is not valid
	 */
	public static @Nullable Pair<String, String> getEntryInfo(final @NotNull String content) {
		final String result = StringConverter
			.cleanMultipleSpaceContent(content);
		Matcher matcher = ENTRY_PATTERN
			.matcher(result);

		// Invalid entry
		if (!matcher.find()) return null;

		// Store temporal results
		String key = StringConverter
			.cleanStringContent(matcher.group(1));
		String value = StringConverter
			.cleanStringContent(matcher.group(2));
		// Return the result
		return Pair.of(key, value);
	}

	/* -----------------------------------------------------
	 * Internal methods
	 * ----------------------------------------------------- */

	/**
	 * Get the current section name
	 *
	 * @param content the content to check
	 * @return the section name
	 */
	private static @NotNull String getSectionName(final @NotNull String content) {
		// Get the attributes and discard that content
		String[] elements = Arrays.stream(content.split(ATTRIBUTE_REGEX))
			.map(String::trim)
			.filter(StringConverter::isNotEmpty)
			.toArray(String[]::new);
		// Return the first value
		return elements.length != 0 ? elements[0]:"";
	}

	/**
	 * Get the current section attributes
	 *
	 * @param content the content to check
	 * @return the section name
	 */
	@Contract(pure = true)
	private static @Unmodifiable @NotNull Map<String, String> getSectionAttributes(final @NotNull String content) {
		// Temporal variables
		Pattern pattern = Pattern.compile(ATTRIBUTE_REGEX);
		Matcher matcher = pattern.matcher(content);
		Map<String, String> result = Collections.mutableMapOf();

		// Find all attributes
		while (matcher.find()) {
			String[] groupValues = matcher
				.group()
				.trim()
				.split(SectionUtils.ASSIGN_ELEMENT);

			// Check if array has 2 values
			if (groupValues.length < 2) continue;

			String key = StringConverter.cleanStringContent(groupValues[0]);
			String value = StringConverter.cleanStringContent(String.join(
				SectionUtils.ASSIGN_ELEMENT,
				Arrays.copyOfRange(groupValues, 1, groupValues.length)
			));
			// Insert the content
			result.put(key, value);
		}

		return result;
	}

}
