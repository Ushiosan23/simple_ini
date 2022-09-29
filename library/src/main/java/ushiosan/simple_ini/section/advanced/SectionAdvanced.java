package ushiosan.simple_ini.section.advanced;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import ushiosan.jvm_utilities.lang.Obj;
import ushiosan.jvm_utilities.lang.collection.elements.Pair;
import ushiosan.simple_ini.section.Section;
import ushiosan.simple_ini.section.SimpleSectionAttributes;

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
	 * @return the attributes as key value
	 */
	@NotNull SectionAttributes getAttributes();

	/**
	 * Set section attribute
	 *
	 * @param key   the attribute name
	 * @param value the attribute value
	 * @return last value of the entry if it already existed or {@link Optional#empty()} otherwise.
	 */
	@NotNull Optional<String> setAttribute(@NotNull CharSequence key, @Nullable CharSequence value);

	/**
	 * Set section attributes
	 *
	 * @param attributes the attributes to insert
	 */
	void setAttributes(@NotNull SectionAttributes attributes);

	/**
	 * Set section attributes
	 *
	 * @param attributes the attributes to insert
	 */
	@SuppressWarnings("unchecked")
	default void setAttributes(Pair<String, String> @NotNull ... attributes) {
		Obj.tryCast(getAttributes(), SimpleSectionAttributes.class, it -> it.putAll(attributes));
	}

	/**
	 * Remove selected attribute from current section
	 *
	 * @param key the attribute to remove
	 * @return the last attribute value if it existed
	 */
	Optional<String> removeAttribute(@NotNull CharSequence key);

	/**
	 * Remove all attributes from current section
	 */
	void clearAttributes();

}
