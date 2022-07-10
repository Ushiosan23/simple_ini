package com.github.ushiosan23.simple_ini.utilities;

import com.github.ushiosan23.jvm.collections.Arr;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;


/**
 * Utilities class for handling section elements
 */
public final class SectionUtils {

	/**
	 * This class cannot be instantiated.
	 * <p>
	 * Singleton or utility class mode.
	 */
	private SectionUtils() {
	}

	/* -----------------------------------------------------
	 * Properties
	 * ----------------------------------------------------- */

	/**
	 * Ini comment identifier
	 */
	public static final String COMMENT_ELEMENT = ";";

	/**
	 * Ini assign identifier
	 */
	public static final String ASSIGN_ELEMENT = "=";

	/**
	 * Ini section wrappers
	 */
	public static final String[] SECTION_WRAPPERS = Arr.of("[", "]");

	/* -----------------------------------------------------
	 * Methods
	 * ----------------------------------------------------- */

	/**
	 * Returns a valid section name.
	 *
	 * @param name The name to check
	 * @return Returns a valid section name
	 */
	public static @NotNull String getValidName(final @NotNull CharSequence name) {
		String data = name
			.toString()
			.trim();
		// Replace all spaces
		return data.replaceAll("\\s+", "-");
	}

	/**
	 * Determines if the content of the line is valid
	 *
	 * @param content The content to check
	 * @return Return {@code true} if content is valid or {@code false} otherwise
	 */
	public static boolean isInvalidContent(final @NotNull CharSequence content) {
		String contentStr = content
			.toString()
			.trim();
		// Validation and result
		return contentStr.startsWith(COMMENT_ELEMENT) || contentStr.isBlank();
	}

	/**
	 * Check if content is a valid ini section
	 *
	 * @param content The content to check
	 * @return Returns {@code true} if section is valid or {@code false} otherwise
	 */
	public static boolean isValidSection(final @NotNull CharSequence content) {
		String contentStr = content
			.toString()
			.trim();
		// Temporal variables
		boolean result = contentStr.startsWith(SECTION_WRAPPERS[0]);
		// Check if section starts with start character
		if (result) {
			contentStr = contentStr.substring(1);
		}
		// Another check
		result = result && contentStr.endsWith(SECTION_WRAPPERS[1]);
		if (result) {
			contentStr = contentStr.substring(0, contentStr.length() - 1);
		}
		return result && !contentStr.trim().isBlank();
	}

	/**
	 * Determines if the content is a valid entry.
	 *
	 * @param content The content to check
	 * @return Returns {@code true} if the content is a valid entry or {@code false} otherwise
	 * @see SectionContentUtils#ENTRY_PATTERN
	 */
	public static boolean isValidEntry(final @NotNull CharSequence content) {
		String contentStr = content
			.toString()
			.trim();
		Matcher matcher = SectionContentUtils
			.ENTRY_PATTERN
			.matcher(contentStr);

		// Validate
		return matcher.find();
	}

}
