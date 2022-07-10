package com.github.ushiosan23.simple_ini.convertion;

import com.github.ushiosan23.jvm.functions.apply.IApply;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Optional;

/**
 * Utility used to convert elements of type {@link CharSequence} to valid {@link Number} type.
 */
public final class NumberConverter {

	/**
	 * This class cannot be instantiated.
	 * <p>
	 * Singleton or utility class mode.
	 */
	private NumberConverter() {
	}

	/* -----------------------------------------------------
	 * Methods
	 * ----------------------------------------------------- */

	/**
	 * Converts a {@link CharSequence} object to a valid {@link Number} object.
	 *
	 * @param content The content to convert
	 * @return Returns a valid number object or {@link Optional#empty()} if the content is not valid
	 */
	public static @NotNull Optional<Number> toNumber(@NotNull CharSequence content) {
		return Optional.ofNullable(toNumberImpl(content));
	}

	/**
	 * Converts a {@link CharSequence} object to a valid {@link Byte} object.
	 *
	 * @param content The content to convert
	 * @return Returns a valid number object or {@link Optional#empty()} if the content is not valid
	 */
	public static @NotNull Optional<Byte> toByte(@NotNull CharSequence content) {
		return toCustomNumberImpl(content, Number::byteValue);
	}

	/**
	 * Converts a {@link CharSequence} object to a valid {@link Short} object.
	 *
	 * @param content The content to convert
	 * @return Returns a valid number object or {@link Optional#empty()} if the content is not valid
	 */
	public static @NotNull Optional<Short> toShort(@NotNull CharSequence content) {
		return toCustomNumberImpl(content, Number::shortValue);
	}

	/**
	 * Converts a {@link CharSequence} object to a valid {@link Integer} object.
	 *
	 * @param content The content to convert
	 * @return Returns a valid number object or {@link Optional#empty()} if the content is not valid
	 */
	public static @NotNull Optional<Integer> toInt(@NotNull CharSequence content) {
		return toCustomNumberImpl(content, Number::intValue);
	}

	/**
	 * Converts a {@link CharSequence} object to a valid {@link Long} object.
	 *
	 * @param content The content to convert
	 * @return Returns a valid number object or {@link Optional#empty()} if the content is not valid
	 */
	public static @NotNull Optional<Long> toLong(@NotNull CharSequence content) {
		return toCustomNumberImpl(content, Number::longValue);
	}

	/**
	 * Converts a {@link CharSequence} object to a valid {@link Float} object.
	 *
	 * @param content The content to convert
	 * @return Returns a valid number object or {@link Optional#empty()} if the content is not valid
	 */
	public static @NotNull Optional<Float> toFloat(@NotNull CharSequence content) {
		return toCustomNumberImpl(content, Number::floatValue);
	}

	/**
	 * Converts a {@link CharSequence} object to a valid {@link Double} object.
	 *
	 * @param content The content to convert
	 * @return Returns a valid number object or {@link Optional#empty()} if the content is not valid
	 */
	public static @NotNull Optional<Double> toDouble(@NotNull CharSequence content) {
		return toCustomNumberImpl(content, Number::doubleValue);
	}

	/* -----------------------------------------------------
	 * Internal methods
	 * ----------------------------------------------------- */

	/**
	 * Converts a {@link CharSequence} object to a valid {@link Number} object.
	 *
	 * @param content The content to convert
	 * @return Returns a valid number object or {@code null} if the content is not valid
	 */
	private static @Nullable @Unmodifiable Number toNumberImpl(@NotNull CharSequence content) {
		// Get content string
		String data = content
			.toString()
			.trim();
		// Try to convert the content
		try {
			return Double.parseDouble(data);
		} catch (Exception ignored) {
			return null;
		}
	}

	/**
	 * Converts a {@link CharSequence} object to any number object, depending on the action.
	 *
	 * @param content The content to convert
	 * @param apply   Transformation action
	 * @return Returns a valid number object or {@link Optional#empty()} if the content is not valid
	 */
	private static <T extends Number> @NotNull Optional<T> toCustomNumberImpl(
		@NotNull CharSequence content,
		@NotNull IApply.WithResult<Number, T> apply
	) {
		// Temporal result
		Number result = toNumberImpl(content);
		T conversion = result == null ? null : apply.invoke(result);
		// Apply invocation
		return result == null ? Optional.empty() :
			Optional.of(conversion);
	}

}
