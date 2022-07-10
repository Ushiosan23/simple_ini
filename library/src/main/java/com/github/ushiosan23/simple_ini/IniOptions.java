package com.github.ushiosan23.simple_ini;

import com.github.ushiosan23.simple_ini.internal.SimpleIniOptions;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Interface used as a data model to generate new implementations.
 */
public interface IniOptions {

	/**
	 * Default options instance
	 */
	IniOptions DEFAULT = createBuilder()
		.build();

	/**
	 * Determines if the object supports advanced elements such as section properties.
	 *
	 * @return Returns {@code true} if advanced is supported or {@code false} otherwise
	 */
	boolean isAdvanced();

	/**
	 * Determines if the object supports multiline entry values.
	 *
	 * @return Returns {@code true} if multiline is supported or {@code false} otherwise
	 */
	boolean supportMultilineValues();

	/**
	 * Generates a new instance of the {@link Builder} class
	 *
	 * @return Returns a new builder instance
	 */
	@Contract(" -> new")
	static @NotNull Builder createBuilder() {
		return new SimpleIniOptions.Builder();
	}

	/**
	 * {@link IniOptions} Builder interface
	 */
	interface Builder {

		/**
		 * Change the advanced mode settings on the object.
		 *
		 * @param status The value to set
		 * @return Returns the current builder instance
		 */
		Builder setAdvanced(boolean status);

		/**
		 * Change the multiline mode settings on the object.
		 *
		 * @param status The value to set
		 * @return Returns the current builder instance
		 */
		Builder setMultiline(boolean status);

		/**
		 * Build the configuration with the provided values or default values instead.
		 *
		 * @return Returns a configured instance of {@link IniOptions}
		 */
		IniOptions build();

	}

}
