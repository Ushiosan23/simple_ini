package ushiosan.simple_ini;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Optional;
import java.util.Set;

import ushiosan.jvm_utilities.lang.Obj;
import static ushiosan.jvm_utilities.lang.Obj.cast;
import ushiosan.jvm_utilities.lang.collection.Collections;
import ushiosan.jvm_utilities.lang.print.annotations.PrintExclude;
import ushiosan.jvm_utilities.lang.print.annotations.PrintOpts;
import ushiosan.simple_ini.internal.reader.IniReader;
import ushiosan.simple_ini.internal.reader.IniWriter;
import ushiosan.simple_ini.section.Section;
import ushiosan.simple_ini.section.SimpleSection;
import ushiosan.simple_ini.section.advanced.SectionAdvanced;


/**
 * Simple class that controls a basic operation of ini files.
 * It also supports advanced behavior and can be extended.
 *
 * @param <T> Generic section type
 */
@PrintOpts(getterAccess = true, getterPrefix = "^(get|is|size|accept)")
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
	private final Set<T> sectionContainer = Collections.mutableSetOf();

	/* -----------------------------------------------------
	 * Constructors
	 * ----------------------------------------------------- */

	/**
	 * Default constructor
	 */
	public SimpleIni() {
		sectionContainer.add(cast(defaultSection));
	}

	/* -----------------------------------------------------
	 * Methods
	 * ----------------------------------------------------- */

	/**
	 * Current ini (reader/writer) options
	 *
	 * @return a current ini options
	 */
	@Override
	public @NotNull IniOptions getOptions() {
		return options;
	}

	/**
	 * The default section element
	 *
	 * @return the default section
	 */
	@PrintExclude
	@Override
	public @NotNull T getDefaultSection() {
		return cast(defaultSection);
	}

	/**
	 * Returns the number of sections within the object, the default section is also counted.
	 *
	 * @return the number of sections within the object
	 */
	@Override
	public int size() {
		return sectionContainer.size();
	}

	/**
	 * Check if current object is empty
	 *
	 * @return {@code true} if current object is empty or {@code false} otherwise
	 */
	@Override
	public boolean isEmpty() {
		return sectionContainer.size() < 2;
	}

	/**
	 * Check if a section exists.
	 *
	 * @param key the section name
	 * @return a {@code true} if section exists or {@code false} otherwise
	 */
	@Override
	public boolean sectionExists(@NotNull CharSequence key) {
		return getSection(key).isPresent();
	}

	/**
	 * Returns the selected section, only if it exists.
	 *
	 * @param key the section name
	 * @return the selected section or {@link Optional#empty()} if not exists
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
	 * @return all ini sections
	 */
	@PrintExclude
	@Override
	public @NotNull @Unmodifiable Set<T> getSections() {
		return Collections.setOf(sectionContainer);
	}

	/**
	 * Insert a new section
	 *
	 * @param section the section to insert
	 */
	@Override
	public void put(Section section) {
		sectionContainer.add(cast(section));
	}

	/**
	 * Remove the specify section
	 *
	 * @param name the section name
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
	 * @param stream        the source stream
	 * @param targetOptions load options
	 * @throws IOException error if something goes wrong
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
	 * @param writer the object to write
	 * @throws IOException error if something goes wrong
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
	 * @return object string representation
	 */
	@Override
	public @NotNull String toString() {
		return Obj.toInstanceString(this);
	}

}
