package com.github.ushiosan23.simple_ini.section;

import com.github.ushiosan23.simple_ini.section.advanced.SectionAdvanced;
import com.github.ushiosan23.simple_ini.section.advanced.SectionAttributes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Default implementation of the {@link AbstractSection} interface.
 * <p>
 * This class can be used as a base for other classes.
 */
public class SimpleSection extends AbstractSection implements SectionAdvanced {

	/* -----------------------------------------------------
	 * Properties
	 * ----------------------------------------------------- */

	/**
	 * Section attributes
	 */
	private final SimpleSectionAttributes attributes;

	/* -----------------------------------------------------
	 * Constructors
	 * ----------------------------------------------------- */

	/**
	 * Primary constructor. This constructor defines the section name and the
	 * default section object (can be {@code null}).
	 *
	 * @param name    The section name
	 * @param section The default section (can be {@code null})
	 */
	public SimpleSection(@NotNull CharSequence name, @Nullable Section section) {
		super(name, section);
		attributes = new SimpleSectionAttributes();
	}

	/**
	 * Secondary constructor. This constructor defines only the section name and the
	 * default section always is {@code null}.
	 *
	 * @param name The section name
	 */
	public SimpleSection(@NotNull CharSequence name) {
		this(name, null);
	}

	/* -----------------------------------------------------
	 * Methods
	 * ----------------------------------------------------- */

	/**
	 * Returns the attributes of the section.
	 * This only applies when advanced mode is activated.
	 *
	 * @return Returns attributes as key value
	 */
	@Override
	public @NotNull SectionAttributes getAttributes() {
		return attributes;
	}

	/**
	 * Set section attribute
	 *
	 * @param key   The attribute name
	 * @param value The attribute value
	 * @return Returns the last value of the entry if it already existed or {@link Optional#empty()} otherwise.
	 */
	@Override
	public @NotNull Optional<String> setAttribute(@NotNull CharSequence key, @Nullable CharSequence value) {
		return attributes.put(key, value);
	}

	/**
	 * Set section attributes
	 *
	 * @param attrs The attributes to insert
	 */
	@Override
	public void setAttributes(@NotNull SectionAttributes attrs) {
		attributes.putAll(attrs.entrySet());
	}

	/**
	 * Remove selected attribute from current section
	 *
	 * @param key The attribute to remove
	 * @return Returns the last attribute value if it existed
	 */
	@Override
	public Optional<String> removeAttribute(@NotNull CharSequence key) {
		return attributes.remove(key);
	}

	/**
	 * Remove all attributes from current section
	 */
	@Override
	public void clearAttributes() {
		attributes.clear();
	}

	/**
	 * Object String representation
	 *
	 * @return Object string representation
	 */
	@Override
	public @NotNull String toString() {
		return "SimpleSection{" +
			"name=" + getName() +
			", entries=" + entrySet() +
			", attributes=" + getAttributes() +
			'}';
	}

}
