package com.github.ushiosan23.simple_ini.conversion;

import com.github.ushiosan23.jvm.collections.Containers;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Utility used to convert elements of type {@link CharSequence} to valid {@link Boolean} type.
 */
public final class BooleanConverter {

	/**
	 * All possible boolean values
	 */
	private static final Map<Character, List<String>> acceptedBooleanValues = Containers.mapOf(
		Containers.entry('t', Containers.listOf("true", "1", "yes", "y")),
		Containers.entry('f', Containers.listOf("false", "0", "no", "n"))
	);

	/**
	 * This class cannot be instantiated.
	 * <p>
	 * Singleton or utility class mode.
	 */
	private BooleanConverter() {
	}

	/* -----------------------------------------------------
	 * Methods
	 * ----------------------------------------------------- */

	/**
	 * Convert a text to a valid boolean.
	 * The method is case-insensitive. So it does not take into account if it is uppercase or lowercase.
	 *
	 * @param content The content to convert
	 * @return Returns a valid boolean result or {@link Optional#empty()} if the content is not a valid boolean
	 */
	public static Optional<Boolean> toBoolean(@NotNull CharSequence content) {
		// Clean value
		String data = content
			.toString()
			.toLowerCase()
			.trim();

		// Check boolean values
		if (acceptedBooleanValues.get('t').contains(data))
			return Optional.of(true);
		if (acceptedBooleanValues.get('f').contains(data))
			return Optional.of(false);

		return Optional.empty();
	}

}
