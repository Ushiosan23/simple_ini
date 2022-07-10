package com.github.ushiosan23.simple_ini;

import com.github.ushiosan23.jvm.collections.Containers;
import com.github.ushiosan23.simple_ini.internal.IniReader;
import com.github.ushiosan23.simple_ini.internal.IniWriter;
import com.github.ushiosan23.simple_ini.section.Section;
import com.github.ushiosan23.simple_ini.section.SimpleSection;
import com.github.ushiosan23.simple_ini.section.advanced.SectionAdvanced;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * Simple class that controls a basic operation of ini files.
 * It also supports advanced behavior and can be extended.
 *
 * @param <T> Generic section type
 */
public class SimpleIni<T extends Section> implements Ini<T> {

	/* -----------------------------------------------------
	 * Properties
	 * ----------------------------------------------------- */

	/**
	 * Default section name
	 */
	public static final String DEFAULT_NAME = "Default";

	/**
	 * Current ini options
	 */
	private IniOptions options = IniOptions.DEFAULT;

	/**
	 * The default ini section.
	 * This section cannot be removed
	 */
	private final SectionAdvanced defaultSection = new SimpleSection(DEFAULT_NAME);

	/**
	 * Section container
	 */
	private final Set<T> sectionContainer = Containers.mutableSetOf();

	/* -----------------------------------------------------
	 * Constructors
	 * ----------------------------------------------------- */

	/**
	 * Default constructor
	 */
	@SuppressWarnings("unchecked")
	public SimpleIni() {
		sectionContainer.add((T) defaultSection);
	}

	/* -----------------------------------------------------
	 * Methods
	 * ----------------------------------------------------- */

	/**
	 * Current ini (reader/writer) options
	 *
	 * @return Returns a current ini options
	 */
	@Override
	public @NotNull IniOptions getOptions() {
		return options;
	}

	/**
	 * The default section element
	 *
	 * @return Returns the default section
	 */
	@SuppressWarnings("unchecked")
	@Override
	public @NotNull T getDefaultSection() {
		return (T) defaultSection;
	}

	/**
	 * Returns the number of sections within the object, the default section is also counted.
	 *
	 * @return Returns the number of sections within the object
	 */
	@Override
	public int size() {
		return sectionContainer.size();
	}

	/**
	 * Check if current object is empty
	 *
	 * @return Return {@code true} if current object is empty or {@code false} otherwise
	 */
	@Override
	public boolean isEmpty() {
		return sectionContainer.size() < 2;
	}

	/**
	 * Check if a section exists.
	 *
	 * @param key The section name
	 * @return Returns a {@code true} if section exists or {@code false} otherwise
	 */
	@Override
	public boolean sectionExists(@NotNull CharSequence key) {
		return getSection(key).isPresent();
	}

	/**
	 * Returns the selected section, only if it exists.
	 *
	 * @param key The section name
	 * @return Returns the selected section or {@link Optional#empty()} if not exists
	 */
	@Override
	public @NotNull Optional<T> getSection(@NotNull CharSequence key) {
		return sectionContainer.stream()
			.filter(it -> it.getName().contentEquals(key))
			.findFirst();
	}

	/**
	 * Returns all ini sections. Included the default section
	 *
	 * @return Returns all ini sections
	 */
	@Override
	public @NotNull @Unmodifiable Set<T> getSections() {
		return Collections.unmodifiableSet(sectionContainer);
	}

	/**
	 * Insert a new section
	 *
	 * @param section The section to insert
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void put(Section section) {
		sectionContainer.add((T) section);
	}

	/**
	 * Remove the specify section
	 *
	 * @param name The section name
	 */
	@Override
	public void remove(@NotNull CharSequence name) {
		String nameStr = name
			.toString()
			.trim();
		// The default section cannot be removed
		if (nameStr.equals(DEFAULT_NAME)) return;
		// Remove the section
		sectionContainer.removeIf(it -> it.getName().equals(nameStr));
	}

	/**
	 * Load content from stream
	 *
	 * @param stream        The source stream
	 * @param targetOptions Load options
	 * @throws IOException Any IO error
	 */
	@Override
	public void load(@NotNull InputStream stream, @NotNull IniOptions targetOptions) throws IOException {
		// Auto close resources
		try (stream) {
			// Change the options
			options = targetOptions;
			// Initialize elements
			IniReader reader = new IniReader(stream, this);
			// Process all content
			reader.processAll();
		}
	}

	/**
	 * Writes the content of the object to an external source.
	 *
	 * @param writer The object to write
	 * @throws IOException Any IO error
	 */
	@Override
	public void store(@NotNull Writer writer) throws IOException {
		// Auto close resources
		try (writer) {
			// Initialize elements
			IniWriter iniWriter = new IniWriter(this, writer);
			iniWriter.storeAll();
		}
	}

	/**
	 * Object string representation
	 *
	 * @return Object string representation
	 */
	@Override
	public @NotNull String toString() {
		return "SimpleIni{" +
			"options=" + options +
			", defaultSection=" + defaultSection +
			", sectionContainer=" + sectionContainer +
			'}';
	}

}
