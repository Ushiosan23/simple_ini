package ushiosan.simple_ini.internal.reader;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import ushiosan.jvm_utilities.lang.Obj;
import ushiosan.simple_ini.Ini;
import ushiosan.simple_ini.section.advanced.SectionAdvanced;

/**
 * Class used to save temporary data when reading an ini file.
 * <p>
 * This data is not used for anything other than what is mentioned above.
 */
public final class IniStorage {

	/* -----------------------------------------------------
	 * Properties
	 * ----------------------------------------------------- */

	/**
	 * Current buffer section
	 */
	private volatile SectionAdvanced currentSection;

	/**
	 * Lass accessed entry name
	 */
	private volatile CharSequence lastAccessEntry;

	/**
	 * Multiline buffer content
	 */
	private final StringBuffer contentBuffer = new StringBuffer();

	/* -----------------------------------------------------
	 * Constructors
	 * ----------------------------------------------------- */

	/**
	 * This class cannot be instantiated.
	 * <p>
	 * Singleton or utility class mode.
	 */
	private IniStorage() {
	}

	/* -----------------------------------------------------
	 * Methods
	 * ----------------------------------------------------- */

	/**
	 * Get current instance section
	 *
	 * @return the current section
	 */
	public @NotNull
	synchronized SectionAdvanced getCurrentSection() {
		return currentSection;
	}

	/**
	 * Set current section
	 *
	 * @param section the section to set
	 */
	public synchronized void setCurrentSection(SectionAdvanced section) {
		currentSection = section;
	}

	/**
	 * Get the last accessed entry name
	 *
	 * @return the last accessed entry name
	 */
	public synchronized Optional<String> getLastAccessEntry() {
		return lastAccessEntry == null ? Optional.empty():
			Optional.of(lastAccessEntry.toString());
	}

	/**
	 * Set the last accessed entry name
	 *
	 * @param entry the entry name
	 */
	public synchronized void setLastAccessEntry(@Nullable CharSequence entry) {
		lastAccessEntry = entry;
	}

	/**
	 * Get the buffer object
	 *
	 * @return Returns the buffer object
	 */
	public @NotNull StringBuffer getBuffer() {
		return contentBuffer;
	}

	/**
	 * Remove all buffer content
	 */
	public void clearBuffer() {
		contentBuffer.setLength(0);
	}

	/**
	 * Check if storage has buffer content
	 *
	 * @return {@code true} if buffer has any data or {@code false} otherwise
	 */
	public boolean hasBufferContent() {
		return contentBuffer.length() != 0;
	}

	/**
	 * Get content buffer
	 *
	 * @return all buffered content
	 */
	public @NotNull String getBufferContent() {
		return contentBuffer.toString();
	}

	/* -----------------------------------------------------
	 * Static methods
	 * ----------------------------------------------------- */

	/**
	 * Generate a ini storage instance
	 *
	 * @param ini the ini instance
	 * @return a new ini storage instance
	 * @see IniStorage
	 */
	@Contract(value = "_ -> new", pure = true)
	public static @NotNull IniStorage of(@NotNull Ini<?> ini) {
		return Obj.also(new IniStorage(), it -> {
			it.setCurrentSection((SectionAdvanced) ini.getDefaultSection());
			it.setLastAccessEntry(null);
		});
	}

}
