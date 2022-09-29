package ushiosan.simple_ini.internal.reader;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Comparator;
import java.util.Date;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ushiosan.jvm_utilities.lang.Obj.canCast;
import static ushiosan.jvm_utilities.lang.Obj.cast;
import ushiosan.jvm_utilities.lang.collection.elements.Pair;
import ushiosan.simple_ini.Ini;
import ushiosan.simple_ini.SimpleIni;
import ushiosan.simple_ini.internal.utilities.SectionUtils;
import ushiosan.simple_ini.section.Section;
import ushiosan.simple_ini.section.advanced.SectionAdvanced;

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
	private static final String CHAR_SPACE = " ";

	/**
	 * String wrapper
	 */
	private static final String STR_WRAPPER = "\"";

	/**
	 * Regular expression to detect any number
	 */
	private static final Pattern NUMBER_PATTERN = Pattern.compile("^\\d+(\\.\\d+)?$");

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
	 * @param iniObj    current ini object
	 * @param writerObj current output element
	 */
	public IniWriter(Ini<?> iniObj, Writer writerObj) {
		ini = iniObj;
		writer = writerObj instanceof BufferedWriter ? (BufferedWriter) writerObj:
			new BufferedWriter(writerObj);
	}

	/* -----------------------------------------------------
	 * Methods
	 * ----------------------------------------------------- */

	/**
	 * Insert all content to the writer element
	 *
	 * @throws IOException error if something goes wrong
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

	/**
	 * Returns the section names in alphabetical order
	 *
	 * @return an array of names in alphabetical order
	 */
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

	/**
	 * Write the document header as a comment and used only
	 * to inform the time the document was generated
	 *
	 * @throws IOException error if something goes wrong
	 */
	private void storeHeaderDocument() throws IOException {
		final Date date = new Date();
		// Insert the header
		writer.append(SectionUtils.COMMENT_ELEMENT)
			.append(CHAR_SPACE)
			.append(date.toString());
		writer.newLine();
	}

	/**
	 * Write an entire section within the document
	 *
	 * @param section the section to write
	 * @throws IOException error if something goes wrong
	 */
	private void storeSection(@NotNull Section section) throws IOException {
		// Validate section
		if (!section.getName().equals(SimpleIni.DEFAULT_NAME))
			storeSectionHeader(section);
		// The other sections
		for (Pair<String, String> entry : section.pairSet()) {
			storeEntry(entry);
		}
	}

	/**
	 * Write inside the document where only the header of a section is inserted
	 *
	 * @param section the section to insert
	 * @throws IOException error if something goes wrong
	 */
	private void storeSectionHeader(@NotNull Section section) throws IOException {
		// The default section
		writer.append(SectionUtils.SECTION_WRAPPERS[0])
			.append(section.getName());
		// Check if section is an advanced type
		if (canCast(section, SectionAdvanced.class) && ini.getOptions().isAdvanced()) {
			SectionAdvanced advanced = cast(section);
			if (!advanced.getAttributes().isEmpty()) {
				writer.append(CHAR_SPACE);
				storeEntryAttributes(advanced);
			}
		}
		// Finish header
		writer.append(SectionUtils.SECTION_WRAPPERS[1]);
		writer.newLine();
	}

	/**
	 * Write the section header attributes
	 *
	 * @param section the section to insert
	 * @throws IOException error if something goes wrong
	 */
	private void storeEntryAttributes(@NotNull SectionAdvanced section) throws IOException {
		// Temporal variables
		Set<Pair<String, String>> pairSet = section
			.getAttributes()
			.pairSet();
		int size = pairSet.size();
		int counter = 0;

		for (Pair<String, String> entry : pairSet) {
			Matcher matcher = NUMBER_PATTERN.matcher(entry.second);
			// Insert the key first
			writer.append(entry.first)
				.append(SectionUtils.ASSIGN_ELEMENT);

			// Check if value is a number
			if (matcher.find()) {
				writer.append(entry.second);
			} else {
				writer.append(STR_WRAPPER)
					.append(entry.second)
					.append(STR_WRAPPER);
			}

			if (counter++ < (size - 1)) writer.append(CHAR_SPACE);
		}
	}

	/**
	 * Write the entry element
	 *
	 * @param entry the entry to insert
	 * @throws IOException error if something goes wrong
	 */
	private void storeEntry(@NotNull Pair<String, String> entry) throws IOException {
		writer.append(entry.first)
			.append(CHAR_SPACE)
			.append(SectionUtils.ASSIGN_ELEMENT)
			.append(CHAR_SPACE)
			.append(entry.second);
		writer.newLine();
	}

}
