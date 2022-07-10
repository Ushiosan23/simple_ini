package com.github.ushiosan23.simple_ini.section.data;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Class used to temporarily save the data of a section, such as name and attributes (only if it is an advanced section).
 */
public final class SectionInfoTmp {

	/**
	 * Section name
	 */
	public final String name;

	/**
	 * Section attributes
	 */
	public final Map<String, String> attributes;

	/* -----------------------------------------------------
	 * Constructors
	 * ----------------------------------------------------- */

	/**
	 * Default constructor
	 *
	 * @param n     The section name
	 * @param attrs The section attributes
	 */
	public SectionInfoTmp(@NotNull String n, @NotNull Map<String, String> attrs) {
		name = n;
		attributes = attrs;
	}

	/**
	 * Check if section is valid
	 *
	 * @return Returns {@code true} if section is valid or {@code false} otherwise
	 */
	public boolean isValid() {
		return !name.isBlank();
	}

	/**
	 * Object string representation
	 *
	 * @return Object string representation
	 */
	@Contract(pure = true)
	@Override
	public @NotNull String toString() {
		return "SectionInfoTmp{" +
			"name='" + name + '\'' +
			", isValid=" + isValid() +
			", attributes=" + attributes +
			'}';
	}

}
