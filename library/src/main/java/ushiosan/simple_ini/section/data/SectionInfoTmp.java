package ushiosan.simple_ini.section.data;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import ushiosan.jvm_utilities.lang.Obj;
import ushiosan.jvm_utilities.lang.print.annotations.PrintOpts;

/**
 * Class used to temporarily save the data of a section, such as name and attributes (only if it is an advanced section).
 */
@PrintOpts(getterAccess = true)
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
	 * @param name       the section name
	 * @param attributes the section attributes
	 */
	public SectionInfoTmp(@NotNull String name, @NotNull Map<String, String> attributes) {
		this.name = name;
		this.attributes = attributes;
	}

	/**
	 * Check if section is valid
	 *
	 * @return {@code true} if section is valid or {@code false} otherwise
	 */
	public boolean isValid() {
		return !name.isBlank();
	}

	/**
	 * Object string representation
	 *
	 * @return object string representation
	 */
	@Contract(pure = true)
	@Override
	public @NotNull String toString() {
		return Obj.toInstanceString(this);
	}

}
