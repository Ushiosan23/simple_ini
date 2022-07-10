package com.github.ushiosan23.simple_ini.internal;

import com.github.ushiosan23.jvm.functions.apply.IApply;
import com.github.ushiosan23.simple_ini.Ini;
import com.github.ushiosan23.simple_ini.convertion.StringConverter;
import com.github.ushiosan23.simple_ini.section.SimpleSection;
import com.github.ushiosan23.simple_ini.section.advanced.SectionAdvanced;
import com.github.ushiosan23.simple_ini.section.data.SectionInfoTmp;
import com.github.ushiosan23.simple_ini.utilities.SectionContentUtils;
import com.github.ushiosan23.simple_ini.utilities.SectionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Optional;

/**
 * Class used to read the ini files and generate the content of the {@link Ini} objects
 */
public final class IniReader {

	/* -----------------------------------------------------
	 * Properties
	 * ----------------------------------------------------- */

	/**
	 * Current reader Stream
	 */
	private final BufferedReader reader;

	/**
	 * Current options
	 */
	private final Ini<?> targetIni;

	/**
	 * Current storage
	 */
	private final IniStorage storage;

	/**
	 * Current stream read
	 */
	private volatile String currentLine;

	/* -----------------------------------------------------
	 * Constructors
	 * ----------------------------------------------------- */

	/**
	 * Default constructor
	 *
	 * @param inputStream The input stream reader
	 * @param ini         The target ini object
	 * @throws IOException IO Error
	 */
	public IniReader(@NotNull InputStream inputStream, @NotNull Ini<?> ini) throws IOException {
		reader = new BufferedReader(new InputStreamReader(inputStream));
		storage = IniStorage.of(ini);
		targetIni = ini;
	}

	/* -----------------------------------------------------
	 * Methods
	 * ----------------------------------------------------- */

	/**
	 * Process all content
	 *
	 * @throws IOException IO Error
	 */
	public void processAll() throws IOException {
		try (reader) {
			while (nextLine()) {
				processLine(currentLine);
			}
			// Check buffer another time
			dynamicCheckBuffer();
		}
	}

	/* -----------------------------------------------------
	 * Internal methods
	 * ----------------------------------------------------- */

	/**
	 * Check if stream contains more lines
	 *
	 * @return Returns {@code true} if stream has another line or {@code false} otherwise
	 * @throws IOException IO Error
	 */
	private boolean nextLine() throws IOException {
		currentLine = reader.readLine();
		return currentLine != null;
	}

	/**
	 * Process the current line
	 *
	 * @param line The current line
	 */
	private void processLine(@NotNull String line) {
		// The buffer needs to be checked 2 times. One at the beginning and one at the end outside the loop
		dynamicCheckBuffer();
		// Check if current line is a comment or empty line
		if (SectionUtils.isInvalidContent(line)) return;

		// Check if line is a valid new section
		validateLine(
			line,
			SectionUtils::isValidSection,
			SectionContentUtils::getSectionInfo,
			this::insertNewSection
		);

		// Check if line is a valid entry
		validateLine(
			line,
			SectionUtils::isValidEntry,
			SectionContentUtils::getEntryInfo,
			this::insertNewEntry
		);

		// Check if all validations are negative
		if (!SectionUtils.isValidSection(line) && !SectionUtils.isValidEntry(line)) {
			// Check config
			if (targetIni.getOptions().supportMultilineValues()) {
				storage.getBuffer().append(line);
			}
		}
	}

	/**
	 * It checks if the buffer contains information and inserts it into the
	 * container if necessary.
	 */
	private void dynamicCheckBuffer() {
		// Check if section contains the current entry
		Optional<String> lastProperty = storage.getLastAccessEntry();
		if (lastProperty.isEmpty() ||
			!storage.getCurrentSection().containsKey(lastProperty.get()) ||
			!storage.hasBufferContent()) return;

		// Insert the buffer to current property
		String previousPartialContent = storage
			.getCurrentSection()
			.getOrDefault(lastProperty.get(), "")
			.trim();
		String newPartialContent = storage
			.getBufferContent()
			.trim();
		// Combine the content
		String newContent = StringConverter.cleanStringContent(String.format(
			"%s %s",
			previousPartialContent,
			newPartialContent
		));

		// Change the entire entry content
		storage.getCurrentSection()
			.put(lastProperty.get(), newContent);
		// Clear the buffer
		storage.clearBuffer();
	}

	/**
	 * Method used to validate lines depending on the given parameters.
	 * The action will only be executed if the other actions are valid.
	 *
	 * @param line      The current line
	 * @param validator Validator action
	 * @param transform Transform action
	 * @param action    Final action
	 * @param <T>       Generic result type
	 */
	private <T> void validateLine(
		@NotNull CharSequence line,
		@NotNull IApply.WithResult<String, Boolean> validator,
		@NotNull IApply.WithResult<String, T> transform,
		@NotNull IApply.EmptyResult<T> action
	) {
		// Clean content
		String lineStr = line
			.toString()
			.trim();
		// Check if line is valid what??
		if (!validator.invoke(lineStr)) return;
		// Transform the line
		action.invoke(transform.invoke(lineStr));
	}

	/**
	 * Method used to validate lines depending on the given parameters,
	 * this method does not transform the result.
	 * The action will only be executed if the other actions are valid.
	 *
	 * @param line      The current line
	 * @param validator Validator action
	 * @param action    Final action
	 */
	private void validateLine(
		@NotNull CharSequence line,
		@NotNull IApply.WithResult<String, Boolean> validator,
		@NotNull IApply.EmptyResult<String> action
	) {
		// Clean content
		String lineStr = line
			.toString()
			.trim();
		// Check if line is valid what??
		if (!validator.invoke(lineStr)) return;
		// Transform the line
		action.invoke(lineStr);
	}

	/* -----------------------------------------------------
	 * Content process methods
	 * ----------------------------------------------------- */

	/**
	 * Insert a new section to the object
	 *
	 * @param sectionInfo The section info
	 */
	private void insertNewSection(@NotNull SectionInfoTmp sectionInfo) {
		// Ignore invalid sections
		if (!sectionInfo.isValid()) return;
		// Generate section
		SectionAdvanced section = new SimpleSection(sectionInfo.name, targetIni.getDefaultSection());
		// Insert section attributes (only if is enabled)
		if (targetIni.getOptions().isAdvanced()) {
			section.getAttributes().putAll(sectionInfo.attributes.entrySet());
		}
		// Insert the section
		targetIni.put(section);
		storage.setCurrentSection(section);
	}

	/**
	 * Create a new entry in the current section.
	 *
	 * @param entry The entry to insert
	 */
	@SuppressWarnings("unchecked")
	private void insertNewEntry(@Nullable Map.Entry<String, String> entry) {
		// Ignore invalid entry elements
		if (entry == null) return;
		// Insert content
		storage.getCurrentSection().putAll(entry);
		storage.setLastAccessEntry(entry.getKey());
	}

}
