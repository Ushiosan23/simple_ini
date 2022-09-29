package ushiosan.simple_ini.internal.reader;

import org.jetbrains.annotations.NotNull;

import ushiosan.jvm_utilities.lang.Obj;
import ushiosan.jvm_utilities.lang.print.annotations.PrintOpts;
import ushiosan.simple_ini.IniOptions;

/**
 * Default implementation of the {@link IniOptions} interface
 */
@PrintOpts(getterAccess = true, getterPrefix = "^(get|is|support)")
public final class SimpleIniOptions implements IniOptions {

	/* -----------------------------------------------------
	 * Properties
	 * ----------------------------------------------------- */

	/**
	 * Multiline option mode
	 */
	private boolean multiline = false;

	/**
	 * Advanced option mode
	 */
	private boolean advanced = false;

	/* -----------------------------------------------------
	 * Constructors
	 * ----------------------------------------------------- */

	/**
	 * This class cannot be instantiated.
	 * <p>
	 * Singleton or utility class mode.
	 */
	private SimpleIniOptions() {
	}

	/* -----------------------------------------------------
	 * Methods
	 * ----------------------------------------------------- */

	/**
	 * Determines if the object supports advanced elements such as section properties.
	 *
	 * @return {@code true} if advanced is supported or {@code false} otherwise
	 */
	@Override
	public boolean isAdvanced() {
		return advanced;
	}

	/**
	 * Determines if the object supports multiline entry values.
	 *
	 * @return {@code true} if multiline is supported or {@code false} otherwise
	 */
	@Override
	public boolean supportMultilineValues() {
		return multiline;
	}

	/**
	 * Object string representation
	 *
	 * @return object string representation
	 */
	@Override
	public @NotNull String toString() {
		return Obj.toInstanceString(this);
	}

	/* -----------------------------------------------------
	 * Internal types
	 * ----------------------------------------------------- */

	/**
	 * Ini options builder
	 */
	public static class Builder implements IniOptions.Builder {

		/**
		 * Current builder result
		 */
		private final SimpleIniOptions result = new SimpleIniOptions();

		/**
		 * Change the advanced mode settings on the object.
		 *
		 * @param status the value to set
		 * @return the current builder instance
		 */
		@Override
		public IniOptions.Builder setAdvanced(boolean status) {
			result.advanced = status;
			return this;
		}

		/**
		 * Change the multiline mode settings on the object.
		 *
		 * @param status the value to set
		 * @return the current builder instance
		 */
		@Override
		public IniOptions.Builder setMultiline(boolean status) {
			result.multiline = status;
			return this;
		}

		/**
		 * Build the configuration with the provided values or default values instead.
		 *
		 * @return a configured instance of {@link IniOptions}
		 */
		@Override
		public IniOptions build() {
			return result;
		}

	}

}
