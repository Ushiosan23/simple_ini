package com.github.ushiosan23.simple_ini;

import com.github.ushiosan23.jvm.collections.Containers;
import com.github.ushiosan23.jvm.io.IOUtils;
import com.github.ushiosan23.simple_ini.section.Section;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Interface used as a data model to generate new implementations.
 *
 * @param <T> Generic section type
 */
public interface Ini<T extends Section> {

	/**
	 * Current ini (reader/writer) options
	 *
	 * @return Returns a current ini options
	 */
	@NotNull IniOptions getOptions();

	/**
	 * The default section element
	 *
	 * @return Returns the default section
	 */
	@NotNull T getDefaultSection();

	/**
	 * Returns the number of sections within the object, the default section is also counted.
	 *
	 * @return Returns the number of sections within the object
	 */
	int size();

	/**
	 * Returns the number of sections inside the object, the default section is not taken into account.
	 *
	 * @return Returns the number of sections within the object
	 */
	default int realSize() {
		return Math.max(size() - 1, 0);
	}

	/**
	 * Check if current object is empty
	 *
	 * @return Return {@code true} if current object is empty or {@code false} otherwise
	 */
	boolean isEmpty();

	/**
	 * Check if a section exists.
	 *
	 * @param key The section name
	 * @return Returns a {@code true} if section exists or {@code false} otherwise
	 */
	boolean sectionExists(@NotNull CharSequence key);

	/**
	 * Returns the selected section, only if it exists.
	 *
	 * @param key The section name
	 * @return Returns the selected section or {@link Optional#empty()} if not exists
	 */
	@NotNull Optional<T> getSection(@NotNull CharSequence key);

	/**
	 * Returns the selected section or default if not exists.
	 *
	 * @param key The section name
	 * @return Returns the selected section or {@link #getDefaultSection()} if not exists
	 * @see #getSection(CharSequence)
	 * @see #getDefaultSection()
	 */
	default @NotNull T getSectionOrDefault(@NotNull CharSequence key) {
		return getSection(key).orElse(getDefaultSection());
	}

	/**
	 * Returns all ini sections. Included the default section
	 *
	 * @return Returns all ini sections
	 */
	@NotNull @Unmodifiable Set<T> getSections();

	/**
	 * Insert a new section
	 *
	 * @param section The section to insert
	 */
	void put(Section section);

	/**
	 * Insert multiple sections
	 *
	 * @param sections The sections to insert
	 */
	default void putAll(Section @NotNull ... sections) {
		for (Section section : sections) {
			put(section);
		}
	}

	/**
	 * Remove the specify section
	 *
	 * @param name The section name
	 */
	void remove(@NotNull CharSequence name);

	/**
	 * Remove multiple sections
	 *
	 * @param names The section names
	 */
	default void removeAll(CharSequence @NotNull ... names) {
		for (CharSequence name : names) {
			remove(name);
		}
	}

	/**
	 * All accepted file extensions.
	 *
	 * @return Returns a list with all valid extensions
	 */
	default @NotNull List<String> acceptedExtensions() {
		return Containers.listOf("ini");
	}

	/**
	 * Load content from file location
	 *
	 * @param location The file location
	 * @param options  Settings for data upload
	 * @throws IOException Any IO error
	 */
	default void load(@NotNull Path location, @NotNull IniOptions options) throws IOException {
		if (Files.isDirectory(location))
			throw new IOException("Invalid regular file. Directory given.");
		// Check extensions
		String extension = IOUtils.getExtension(location);
		if (!acceptedExtensions().contains(extension))
			throw new IOException(String.format("Invalid file extension. Only \"%s\" accepted", acceptedExtensions()));
		// Load content
		load(Files.newInputStream(location), options);
	}

	/**
	 * Load content from stream
	 *
	 * @param stream  The source stream
	 * @param options Settings for data upload
	 * @throws IOException Any IO error
	 */
	void load(@NotNull InputStream stream, @NotNull IniOptions options) throws IOException;

	/**
	 * Load content from file location
	 *
	 * @param location The file location
	 * @throws IOException Any IO error
	 */
	default void load(@NotNull Path location) throws IOException {
		load(location, getOptions());
	}

	/**
	 * Load content from stream
	 *
	 * @param stream The source stream
	 * @throws IOException Any IO error
	 */
	default void load(@NotNull InputStream stream) throws IOException {
		load(stream, IniOptions.DEFAULT);
	}

	/**
	 * Writes the content of the object to an external source.
	 *
	 * @param location The file to write
	 * @throws IOException Any IO error
	 */
	default void store(@NotNull Path location) throws IOException {
		store(Files.newOutputStream(location, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING));
	}

	/**
	 * Writes the content of the object to an external source.
	 *
	 * @param stream The stream to write
	 * @throws IOException Any IO error
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
	 * @param writer The object to write
	 * @throws IOException Any IO error
	 */
	void store(@NotNull Writer writer) throws IOException;

}
