package ushiosan.simple_ini.section;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import ushiosan.jvm_utilities.lang.Obj;
import ushiosan.jvm_utilities.lang.print.annotations.PrintOpts;
import ushiosan.simple_ini.section.advanced.SectionAttributes;

/**
 * Default implementation of the {@link SectionAttributes} interface.
 *
 * @see AbstractSection
 * @see Section
 */
@PrintOpts(getterAccess = true, getterPrefix = "^(get|is|pair)")
public class SimpleSectionAttributes extends AbstractSection implements SectionAttributes {

	/* -----------------------------------------------------
	 * Constructors
	 * ----------------------------------------------------- */

	/**
	 * Primary constructor.
	 */
	public SimpleSectionAttributes() {
		super("", null);
	}

	/* -----------------------------------------------------
	 * Invalidate methods
	 * ----------------------------------------------------- */

	/**
	 * Rename the section
	 *
	 * @param newName the name of the section
	 * @throws UnsupportedOperationException unsupported action
	 */
	@Override
	public void setName(@NotNull CharSequence newName) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Set a default session
	 *
	 * @param section the default section object
	 * @return the last value of the entry if it already existed or {@link Optional#empty()} otherwise.
	 * @throws UnsupportedOperationException unsupported action
	 */
	@Override
	public @NotNull Optional<Section> setDefaultSection(@Nullable Section section) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Get the current section name
	 *
	 * @return the current section name
	 * @throws UnsupportedOperationException unsupported action
	 */
	@Override
	public @NotNull String getName() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the default section to search for if it does not exist in the current section.
	 *
	 * @return the default section or {@link Optional#empty()} if not defined
	 * @throws UnsupportedOperationException unsupported action
	 */
	@Override
	public @NotNull Optional<Section> getDefaultSection() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Object string representation
	 *
	 * @return object string representation
	 */
	@Override
	public String toString() {
		return Obj.toInstanceString(this);
	}

}
