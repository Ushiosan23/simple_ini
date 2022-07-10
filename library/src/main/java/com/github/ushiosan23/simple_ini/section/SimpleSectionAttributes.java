package com.github.ushiosan23.simple_ini.section;

import com.github.ushiosan23.simple_ini.section.advanced.SectionAttributes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Default implementation of the {@link SectionAttributes} interface.
 *
 * @see AbstractSection
 * @see Section
 */
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
	 * @param newName The name of the section
	 * @throws UnsupportedOperationException Unsupported action
	 */
	@Override
	public void setName(@NotNull CharSequence newName) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Set a default session
	 *
	 * @param section The default section object
	 * @return Returns the last value of the entry if it already existed or {@link Optional#empty()} otherwise.
	 * @throws UnsupportedOperationException Unsupported action
	 */
	@Override
	public @NotNull Optional<Section> setDefaultSection(@Nullable Section section) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Get the current section name
	 *
	 * @return Returns the current section name
	 * @throws UnsupportedOperationException Unsupported action
	 */
	@Override
	public @NotNull String getName() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the default section to search for if it does not exist in the current section.
	 *
	 * @return Returns the default section or {@link Optional#empty()} if not defined
	 * @throws UnsupportedOperationException Unsupported action
	 */
	@Override
	public @NotNull Optional<Section> getDefaultSection() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Object string representation
	 *
	 * @return Object string representation
	 */
	@Override
	public String toString() {
		return "SimpleSectionAttributes{" +
			"entries=" + entrySet() +
			"}";
	}

}
