package com.github.ushiosan23.simple_ini.conversion;

import com.github.ushiosan23.jvm.collections.Arr;
import com.github.ushiosan23.jvm.collections.Containers;
import com.github.ushiosan23.jvm.functions.apply.IApply;
import org.intellij.lang.annotations.RegExp;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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
	 * @param content   The content to convert
	 * @param separator The content separator
	 * @return Returns a {@link List} with all elements
	 */
	public static @NotNull @Unmodifiable List<String> toList(
		@NotNull CharSequence content,
		@NotNull @RegExp String separator
	) {
		// Convert the content
		return toCustomCollection(content, separator, Containers::listOf);
	}

	/**
	 * Generates a set based on the content and the given separator
	 *
	 * @param content   The content to convert
	 * @param separator The content separator
	 * @return Returns a {@link Set} with all elements
	 */
	public static @NotNull @Unmodifiable Set<String> toSet(
		@NotNull CharSequence content,
		@NotNull @RegExp String separator
	) {
		// Convert the content
		return toCustomCollection(content, separator, Containers::setOf);
	}

	/**
	 * Returns a collection depending on the given content and action.
	 *
	 * @param content The content to convert
	 * @param regex   Regular expression separator
	 * @param apply   Action to execute for the result content
	 * @param <T>     Generic output type
	 * @return Returns a collection with all the elements inside it.
	 */
	public static <T extends Collection<?>> @NotNull T toCustomCollection(
		@NotNull CharSequence content,
		@NotNull @RegExp String regex,
		@NotNull IApply.WithResult<String[], T> apply
	) {
		String realContent = content.toString().trim();
		// Generate content
		try (var stream = Arrays.stream(realContent.split(regex))) {
			return apply.invoke(stream
				.map(String::trim)
				.toArray(String[]::new)
			);
		} catch (Exception e) {
			return apply.invoke(Arr.of());
		}
	}

}
