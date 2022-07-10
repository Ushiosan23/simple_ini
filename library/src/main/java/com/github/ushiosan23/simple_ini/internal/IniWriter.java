package com.github.ushiosan23.simple_ini.internal;

import com.github.ushiosan23.simple_ini.Ini;
import com.github.ushiosan23.simple_ini.SimpleIni;
import com.github.ushiosan23.simple_ini.section.Section;
import com.github.ushiosan23.simple_ini.section.advanced.SectionAdvanced;
import com.github.ushiosan23.simple_ini.utilities.SectionUtils;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class used to write ini files with all the contents of
 * the current {@link Ini} object.
 */
public final class IniWriter {

	/* -----------------------------------------------------
	 * Properties
	 * ----------------------------------------------------- */

	/**
	 * New space
	 */
	private final String CHAR_SPACE = " ";

	/**
	 * String wrapper
	 */
	private final String STR_WRAP = "\"";

	/**
	 * Regular expression to detect any number
	 */
	private final Pattern NUMBER_PATTERN = Pattern.compile("^\\d+(\\.\\d+)?$");

	/**
	 * Current ini instance
	 */
	private final Ini<?> ini;

	/**
	 * Current output element
	 */
	private final BufferedWriter writer;

	/* -----------------------------------------------------
	 * Constructors
	 * ----------------------------------------------------- */

	/**
	 * Default constructor
	 *
	 * @param iniObj    Current ini object
	 * @param writerObj Current output element
	 */
	public IniWriter(Ini<?> iniObj, Writer writerObj) {
		ini = iniObj;
		writer = writerObj instanceof BufferedWriter ? (BufferedWriter) writerObj :
			new BufferedWriter(writerObj);
	}

	/* -----------------------------------------------------
	 * Methods
	 * ----------------------------------------------------- */

	/**
	 * Insert all content to the writer element
	 *
	 * @throws IOException Any IO error
	 */
	public void storeAll() throws IOException {
		// We must position the default section first.
		Section[] outSectionOrder = getOrderedSections();
		// Store a little header document
		storeHeaderDocument();
		// Iterate all sections
		for (int i = 0; i < outSectionOrder.length; i++) {
			storeSection(outSectionOrder[i]);
			// Insert new line
			if (i < outSectionOrder.length - 1) writer.newLine();
		}
		// Flush all content
		writer.flush();
	}

	/* -----------------------------------------------------
	 * Internal methods
	 * ----------------------------------------------------- */

	private Section @NotNull [] getOrderedSections() {
		// Generate output array
		final int size = ini.size();
		Section[] result = new Section[size];
		Section[] sectionsWithoutDefault = ini.getSections()
			.stream()
			.filter(it -> !it.getName().equals(SimpleIni.DEFAULT_NAME))
			.sorted(Comparator.comparing((Section o) -> o.getName()))
			.toArray(Section[]::new);
		// The first element always be the default section
		result[0] = ini.getDefaultSection();
		// Copy the other sections
		System.arraycopy(sectionsWithoutDefault, 0, result, 1, size - 1);
		return result;
	}

	private void storeHeaderDocument() throws IOException {
		final Date date = new Date();
		// Insert the header
		writer.append(SectionUtils.COMMENT_ELEMENT)
			.append(CHAR_SPACE)
			.append(date.toString());
		writer.newLine();
	}

	private void storeSection(@NotNull Section section) throws IOException {
		// Validate section
		if (!section.getName().equals(SimpleIni.DEFAULT_NAME))
			storeSectionHeader(section);
		// The other sections
		for (Map.Entry<String, String> entry : section.entrySet()) {
			storeEntry(entry);
		}
	}

	private void storeSectionHeader(@NotNull Section section) throws IOException {
		// The default section
		writer.append(SectionUtils.SECTION_WRAPPERS[0])
			.append(section.getName());
		// Check if section is an advanced type
		if (section instanceof SectionAdvanced && ini.getOptions().isAdvanced()) {
			SectionAdvanced advanced = (SectionAdvanced) section;
			if (!advanced.getAttributes().isEmpty()) {
				writer.append(CHAR_SPACE);
				storeEntryAttributes(advanced);
			}
		}
		// Finish header
		writer.append(SectionUtils.SECTION_WRAPPERS[1]);
		writer.newLine();
	}

	private void storeEntryAttributes(@NotNull SectionAdvanced section) throws IOException {
		// Temporal variables
		var entrySet = section
			.getAttributes()
			.entrySet();
		int size = entrySet.size();
		int counter = 0;

		for (Map.Entry<String, String> entry : entrySet) {
			Matcher matcher = NUMBER_PATTERN.matcher(entry.getValue());
			// Insert the key first
			writer.append(entry.getKey())
				.append(SectionUtils.ASSIGN_ELEMENT);

			// Check if value is a number
			if (matcher.find()) {
				writer.append(entry.getValue());
			} else {
				writer.append(STR_WRAP)
					.append(entry.getValue())
					.append(STR_WRAP);
			}

			if (counter++ < (size - 1)) writer.append(CHAR_SPACE);
		}
	}

	private void storeEntry(@NotNull Map.Entry<String, String> entry) throws IOException {
		writer.append(entry.getKey())
			.append(CHAR_SPACE)
			.append(SectionUtils.ASSIGN_ELEMENT)
			.append(CHAR_SPACE)
			.append(entry.getValue());
		writer.newLine();
	}

}
