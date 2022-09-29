package ushiosan.simple_ini.conversion;

import org.intellij.lang.annotations.RegExp;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import ushiosan.jvm_utilities.function.Apply;
import ushiosan.jvm_utilities.lang.collection.Arrs;
import ushiosan.jvm_utilities.lang.collection.Collections;

/**
 * Utility used to convert elements of type {@link CharSequence} to collection of
 * data known as {@link List} or {@link Set}, in addition to a series of extra utilities to implement more elements.
 */
public final class CollectionConverter {

	/**
	 * This class cannot be instantiated.
	 * <p>
	 * Singleton or utility class mode.
	 */
	private CollectionConverter() {
	}

	/* -----------------------------------------------------
	 * Methods
	 * ----------------------------------------------------- */

	/**
	 * Generates a list based on the content and the given separator
	 *
	 * @param content   the content to convert
	 * @param separator the content separator
	 * @return a {@link List} with all elements
	 */
	public static @NotNull @Unmodifiable List<String> toList(
		@NotNull CharSequence content,
		@NotNull @RegExp String separator
	) {
		// Convert the content
		return toCustomCollection(content, separator, Collections::listOf);
	}

	/**
	 * Generates a set based on the content and the given separator
	 *
	 * @param content   the content to convert
	 * @param separator the content separator
	 * @return a {@link Set} with all elements
	 */
	public static @NotNull @Unmodifiable Set<String> toSet(
		@NotNull CharSequence content,
		@NotNull @RegExp String separator
	) {
		// Convert the content
		return toCustomCollection(content, separator, Collections::setOf);
	}

	/**
	 * Returns a collection depending on the given content and action.
	 *
	 * @param content the content to convert
	 * @param regex   regular expression separator
	 * @param apply   action to execute for the result content
	 * @param <T>     generic output type
	 * @return a collection with all the elements inside it.
	 */
	public static <T extends Collection<?>> @NotNull T toCustomCollection(
		@NotNull CharSequence content,
		@NotNull @RegExp String regex,
		@NotNull Apply.Result<String[], T> apply
	) {
		String realContent = content.toString().trim();
		// Generate content
		try (var stream = Arrays.stream(realContent.split(regex))) {
			return apply.apply(stream
				.map(String::trim)
				.toArray(String[]::new)
			);
		} catch (Exception e) {
			return apply.apply(Arrs.of());
		}
	}

}
