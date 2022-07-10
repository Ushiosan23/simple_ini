package com.github.ushiosan23.simple_ini.convertion;

import org.intellij.lang.annotations.RegExp;
import org.jetbrains.annotations.NotNull;

/**
 * Utility used to handle content of {@link CharSequence} type
 */
public final class StringConverter {

	/**
	 * This class cannot be instantiated.
	 * <p>
	 * Singleton or utility class mode.
	 */
	private StringConverter() {
	}

	/* -----------------------------------------------------
	 * Properties
	 * ----------------------------------------------------- */

	/**
	 * Regular expression to detect multiple spaces on the string
	 */
	@RegExp
	private static final String MULTIPLE_SPACE_REGEX = "\\s+";

	/* -----------------------------------------------------
	 * Methods
	 * ----------------------------------------------------- */

	/**
	 * Check if content is not empty
	 *
	 * @param content The content to check
	 * @return Returns {@code true} if the content is not empty or {@code false} otherwise
	 */
	public static boolean isNotEmpty(@NotNull CharSequence content) {
		return !content.toString().isBlank();
	}

	/**
	 * Remove the quotes that determine if a string is a text
	 *
	 * @param content The content to convert
	 * @return Returns a clean content
	 */
	public static String cleanStringContent(@NotNull CharSequence content) {
		String strContent = cleanMultipleSpaceContent(content
			.toString()
			.trim()
		);
		// Clean the string
		return strContent.startsWith("\"") && strContent.endsWith("\"") ?
			strContent.substring(1, strContent.length() - 1) :
			strContent;
	}

	/**
	 * Check if the content has multiple unnecessary spaces and only keeps one.
	 *
	 * @param content The content to convert
	 * @return Returns a clean content
	 */
	public static @NotNull String cleanMultipleSpaceContent(@NotNull CharSequence content) {
		String strContent = content
			.toString()
			.trim();
		// Replace the content
		return strContent.replaceAll(MULTIPLE_SPACE_REGEX, " ");
	}

}
