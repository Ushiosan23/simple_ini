package ushiosan.simple_ini.internal.reader;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;

import ushiosan.jvm_utilities.function.Apply;
import ushiosan.jvm_utilities.lang.collection.elements.Pair;
import ushiosan.simple_ini.Ini;
import ushiosan.simple_ini.conversion.StringConverter;
import ushiosan.simple_ini.internal.utilities.SectionContentUtils;
import ushiosan.simple_ini.internal.utilities.SectionUtils;
import ushiosan.simple_ini.section.SimpleSection;
import ushiosan.simple_ini.section.advanced.SectionAdvanced;
import ushiosan.simple_ini.section.data.SectionInfoTmp;

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
	 * @param inputStream the input stream reader
	 * @param ini         the target ini object
	 * @throws IOException error if something goes wrong
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
	 * @throws IOException error if something goes wrong
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
	 * @return {@code true} if stream has another line or {@code false} otherwise
	 * @throws IOException error if something goes wrong
	 */
	private boolean nextLine() throws IOException {
		currentLine = reader.readLine();
		return currentLine != null;
	}

	/**
	 * Process the current line
	 *
	 * @param line the current line
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
	 * @param line      the current line
	 * @param validator validator action
	 * @param transform transform action
	 * @param action    final action
	 * @param <T>       generic result type
	 */
	private <T> void validateLine(
		@NotNull CharSequence line,
		@NotNull Apply.Result<String, Boolean> validator,
		@NotNull Apply.Result<String, T> transform,
		@NotNull Apply.Empty<T> action
	) {
		// Clean content
		String lineStr = line
			.toString()
			.trim();
		// Check if line is valid what??
		if (!validator.apply(lineStr)) return;
		// Transform the line
		action.apply(transform.apply(lineStr));
	}

	/**
	 * Method used to validate lines depending on the given parameters,
	 * this method does not transform the result.
	 * The action will only be executed if the other actions are valid.
	 *
	 * @param line      the current line
	 * @param validator validator action
	 * @param action    final action
	 */
	private void validateLine(
		@NotNull CharSequence line,
		@NotNull Apply.Result<String, Boolean> validator,
		@NotNull Apply.Empty<String> action
	) {
		// Clean content
		String lineStr = line
			.toString()
			.trim();
		// Check if line is valid what??
		if (!validator.apply(lineStr)) return;
		// Transform the line
		action.apply(lineStr);
	}

	/* -----------------------------------------------------
	 * Content process methods
	 * ----------------------------------------------------- */

	/**
	 * Insert a new section to the object
	 *
	 * @param sectionInfo the section info
	 */
	private void insertNewSection(@NotNull SectionInfoTmp sectionInfo) {
		// Ignore invalid sections
		if (!sectionInfo.isValid()) return;
		// Generate section
		SectionAdvanced section = new SimpleSection(sectionInfo.name, targetIni.getDefaultSection());
		// Insert section attributes (only if is enabled)
		if (targetIni.getOptions().isAdvanced()) {
			section.getAttributes().putAll(Pair.extractPairs(sectionInfo.attributes));
		}
		// Insert the section
		targetIni.put(section);
		storage.setCurrentSection(section);
	}

	/**
	 * Create a new pair in the current section.
	 *
	 * @param pair the pair to insert
	 */
	@SuppressWarnings("unchecked")
	private void insertNewEntry(@Nullable Pair<String, String> pair) {
		// Ignore invalid pair elements
		if (pair == null) return;
		// Insert content
		storage.getCurrentSection().putAll(pair);
		storage.setLastAccessEntry(pair.first);
	}

}
