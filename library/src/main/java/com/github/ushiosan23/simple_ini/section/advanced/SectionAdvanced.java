package com.github.ushiosan23.simple_ini.section.advanced;

import com.github.ushiosan23.jvm.base.Obj;
import com.github.ushiosan23.simple_ini.section.Section;
import com.github.ushiosan23.simple_ini.section.SimpleSectionAttributes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

/**
 * Interface used as a data model to generate new implementations.
 * <p>
 * This interface determines that the sections that implement it have attributes.
 */
public interface SectionAdvanced extends Section {

	/**
	 * Returns the attributes of the section.
	 * This only applies when advanced mode is activated.
	 *
	 * @return Returns attributes as key value
	 */
	@NotNull SectionAttributes getAttributes();

	/**
	 * Set section attribute
	 *
	 * @param key   The attribute name
	 * @param value The attribute value
	 * @return Returns the last value of the entry if it already existed or {@link Optional#empty()} otherwise.
	 */
	@NotNull Optional<String> setAttribute(@NotNull CharSequence key, @Nullable CharSequence value);

	/**
	 * Set section attributes
	 *
	 * @param attributes The attributes to insert
	 */
	void setAttributes(@NotNull SectionAttributes attributes);

	/**
	 * Set section attributes
	 *
	 * @param attributes The attributes to insert
	 */
	@SuppressWarnings({"unused", "unchecked"})
	default void setAttributes(Map.Entry<String, String> @NotNull ... attributes) {
		Obj.tryCast(getAttributes(), SimpleSectionAttributes.class, it -> it.putAll(attributes));
	}

	/**
	 * Remove selected attribute from current section
	 *
	 * @param key The attribute to remove
	 * @return Returns the last attribute value if it existed
	 */
	Optional<String> removeAttribute(@NotNull CharSequence key);

	/**
	 * Remove all attributes from current section
	 */
	void clearAttributes();

}
