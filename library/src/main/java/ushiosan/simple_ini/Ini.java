package ushiosan.simple_ini;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import ushiosan.jvm_utilities.lang.collection.Collections;
import ushiosan.jvm_utilities.lang.io.IO;
import ushiosan.simple_ini.section.Section;

/**
 * Interface used as a data model to generate new implementations.
 *
 * @param <T> generic section type
 */
public interface Ini<T extends Section> {

	/**
	 * Current ini (reader/writer) options
	 *
	 * @return a current ini options
	 */
	@NotNull IniOptions getOptions();

	/**
	 * The default section element
	 *
	 * @return the default section
	 */
	@NotNull T getDefaultSection();

	/**
	 * Returns the number of sections within the object, the default section is also counted.
	 *
	 * @return the number of sections within the object
	 */
	int size();

	/**
	 * Returns the number of sections inside the object, the default section is not taken into account.
	 *
	 * @return the number of sections within the object
	 */
	default int realSize() {
		return Math.max(size() - 1, 0);
	}

	/**
	 * Check if current object is empty
	 *
	 * @return {@code true} if current object is empty or {@code false} otherwise
	 */
	boolean isEmpty();

	/**
	 * Check if a section exists.
	 *
	 * @param key the section name
	 * @return a {@code true} if section exists or {@code false} otherwise
	 */
	boolean sectionExists(@NotNull CharSequence key);

	/**
	 * Returns the selected section, only if it exists.
	 *
	 * @param key the section name
	 * @return the selected section or {@link Optional#empty()} if not exists
	 */
	@NotNull Optional<T> getSection(@NotNull CharSequence key);

	/**
	 * Returns the selected section or default if not exists.
	 *
	 * @param key the section name
	 * @return the selected section or {@link #getDefaultSection()} if not exists
	 * @see #getSection(CharSequence)
	 * @see #getDefaultSection()
	 */
	default @NotNull T getSectionOrDefault(@NotNull CharSequence key) {
		return getSection(key).orElse(getDefaultSection());
	}

	/**
	 * Returns all ini sections. Included the default section
	 *
	 * @return all ini sections
	 */
	@NotNull @Unmodifiable Set<T> getSections();

	/**
	 * Insert a new section
	 *
	 * @param section the section to insert
	 */
	void put(Section section);

	/**
	 * Insert multiple sections
	 *
	 * @param sections the sections to insert
	 */
	default void putAll(Section @NotNull ... sections) {
		for (Section section : sections) {
			put(section);
		}
	}

	/**
	 * Remove the specify section
	 *
	 * @param name the section name
	 */
	void remove(@NotNull CharSequence name);

	/**
	 * Remove multiple sections
	 *
	 * @param names the section names
	 */
	default void removeAll(CharSequence @NotNull ... names) {
		for (CharSequence name : names) {
			remove(name);
		}
	}

	/**
	 * All accepted file extensions.
	 *
	 * @return a list with all valid extensions
	 */
	default @NotNull List<String> acceptedExtensions() {
		return Collections.listOf("ini");
	}

	/**
	 * Load content from file location
	 *
	 * @param location the file location
	 * @param options  settings for data upload
	 * @throws IOException error if something goes wrong. Invalid file, invalid extension, etc...
	 */
	default void load(@NotNull Path location, @NotNull IniOptions options) throws IOException {
		if (Files.isDirectory(location))
			throw new IOException("Invalid regular file. Directory given.");
		// Check extensions
		Optional<String> extension = IO.getExtension(location);
		if (extension.isEmpty() || !acceptedExtensions().contains(extension.get()))
			throw new IOException(String.format("Invalid file extension. Only \"%s\" accepted", acceptedExtensions()));
		// Load content
		load(Files.newInputStream(location), options);
	}

	/**
	 * Load content from stream
	 *
	 * @param stream  the source stream
	 * @param options settings for data upload
	 * @throws IOException error if something goes wrong. Invalid file, invalid extension, etc...
	 */
	void load(@NotNull InputStream stream, @NotNull IniOptions options) throws IOException;

	/**
	 * Load content from file location
	 *
	 * @param location the file location
	 * @throws IOException error if something goes wrong. Invalid file, invalid extension, etc...
	 */
	default void load(@NotNull Path location) throws IOException {
		load(location, getOptions());
	}

	/**
	 * Load content from stream
	 *
	 * @param stream the source stream
	 * @throws IOException error if something goes wrong. Invalid file, invalid extension, etc...
	 */
	default void load(@NotNull InputStream stream) throws IOException {
		load(stream, IniOptions.DEFAULT);
	}

	/**
	 * Writes the content of the object to an external source.
	 *
	 * @param location the file to write
	 * @throws IOException error if something goes wrong. Error to store
	 *                     file information
	 */
	default void store(@NotNull Path location) throws IOException {
		store(Files.newOutputStream(location, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING));
	}

	/**
	 * Writes the content of the object to an external source.
	 *
	 * @param stream the stream to write
	 * @throws IOException error if something goes wrong. Error to store
	 *                     file information
	 */
	default void store(@NotNull OutputStream stream) throws IOException {
		// Use try to manage autocloseable elements
		try (stream) {
			store(new OutputStreamWriter(stream));
		}
	}

	/**
	 * Writes the content of the object to an external source.
	 *
	 * @param writer the object to write
	 * @throws IOException error if something goes wrong. Error to store
	 *                     file information
	 */
	void store(@NotNull Writer writer) throws IOException;

}
