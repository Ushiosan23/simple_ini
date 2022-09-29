package ushiosan.simple_ini.section;

import org.intellij.lang.annotations.RegExp;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import ushiosan.jvm_utilities.lang.collection.elements.Pair;

/**
 * Interface used as a data model to generate new implementations.
 */
public interface Section {

	/**
	 * Get the current section name
	 *
	 * @return the current section name
	 */
	@NotNull String getName();

	/**
	 * Returns the default section to search for if it does not exist in the current section.
	 *
	 * @return the default section or {@link Optional#empty()} if not defined
	 */
	@NotNull Optional<Section> getDefaultSection();

	/**
	 * Returns size of entries that the current session has
	 *
	 * @return the size of entries
	 */
	int size();

	/**
	 * Determines if the current session does not contain any entries.
	 *
	 * @return {@code true} if the section is empty or {@code false} otherwise
	 */
	boolean isEmpty();

	/**
	 * Method to check if an entry exists or not.
	 *
	 * @param key the entry name
	 * @return {@code true} if the entry exists or {@code false} otherwise
	 */
	boolean containsKey(@NotNull CharSequence key);

	/**
	 * Returns the current content of the selected entry.
	 *
	 * @param key the entry name
	 * @return the entry content or {@link Optional#empty()} if entry not exists
	 */
	@NotNull Optional<String> get(@NotNull CharSequence key);

	/**
	 * Returns the current content of the selected entry.
	 *
	 * @param key the entry name
	 * @return the entry content or {@link Optional#empty()} if entry not exists
	 */
	@NotNull Optional<Number> getAsNumber(@NotNull CharSequence key);

	/**
	 * Returns the current content of the selected entry.
	 *
	 * @param key the entry name
	 * @return the entry content or {@link Optional#empty()} if entry not exists
	 */
	@NotNull Optional<Boolean> getAsBoolean(@NotNull CharSequence key);

	/**
	 * Returns the current content as {@link List} of the selected entry.
	 *
	 * @param key   the entry name
	 * @param regex the content separator
	 * @return the entry content or {@link Optional#empty()} if entry not exists
	 */
	@NotNull List<String> getAsList(@NotNull CharSequence key, @NotNull @RegExp String regex);

	/**
	 * Returns the current content as {@link Set} of the selected entry.
	 *
	 * @param key   the entry name
	 * @param regex the content separator
	 * @return the entry content or {@link Optional#empty()} if entry not exists
	 */
	@NotNull Set<String> getAsSet(@NotNull CharSequence key, @NotNull @RegExp String regex);

	/**
	 * Returns all entry names in the current section.
	 *
	 * @return a {@link Set} with all entry names
	 */
	@NotNull Set<String> keys();

	/**
	 * Returns all entry values in the current section.
	 *
	 * @return a {@link Set} with all entry values
	 */
	@NotNull Set<String> values();

	/**
	 * Returns all entries in the current section.
	 *
	 * @return a {@link Set} with all entries
	 */
	@NotNull Set<Pair<String, String>> pairSet();

	/**
	 * Rename the section
	 *
	 * @param newName the name of the section
	 */
	void setName(@NotNull CharSequence newName);

	/**
	 * Insert a new entry in the section.
	 *
	 * @param key   the entry name
	 * @param value the entry value
	 * @return the last value of the entry if it already existed or {@link Optional#empty()} otherwise.
	 */
	@NotNull Optional<String> put(@NotNull CharSequence key, @Nullable CharSequence value);

	/**
	 * Insert a multiple entries in the section.
	 *
	 * @param elements the entries to insert
	 * @see #put(CharSequence, CharSequence)
	 */
	void putAll(@NotNull Collection<Pair<String, String>> elements);

	/**
	 * Insert a multiple entries in the section.
	 *
	 * @param elements the entries to insert
	 * @see #put(CharSequence, CharSequence)
	 */
	@SuppressWarnings("unchecked")
	void putAll(Pair<String, String> @NotNull ... elements);

	/**
	 * Remove a section entry
	 *
	 * @param key the entry to remove
	 * @return the last value of the entry if it already existed {@link Optional#empty()} otherwise.
	 */
	Optional<String> remove(@NotNull CharSequence key);

	/**
	 * Remove all entries
	 */
	void clear();

	/**
	 * Set a default session
	 *
	 * @param section the default section object
	 * @return the last value of the entry if it already existed or {@link Optional#empty()} otherwise.
	 */
	@NotNull Optional<Section> setDefaultSection(@Nullable Section section);

	/* -----------------------------------------------------
	 * Default methods
	 * ----------------------------------------------------- */

	/**
	 * Returns the current content of the selected entry.
	 *
	 * @param key          the entry name
	 * @param defaultValue a default value if the entry does not exist
	 * @return the current content or {@code defaultValue} if entry does not exist
	 */
	default String getOrDefault(@NotNull CharSequence key, @NotNull CharSequence defaultValue) {
		return get(key).orElse(defaultValue.toString());
	}

	/**
	 * Returns the current content of the selected entry.
	 *
	 * @param key          the entry name
	 * @param defaultValue a default value if the entry does not exist
	 * @return the current content or {@code defaultValue} if entry does not exist
	 */
	default Number getAsNumberOrDefault(@NotNull CharSequence key, @NotNull Number defaultValue) {
		return getAsNumber(key).orElse(defaultValue);
	}

	/**
	 * Returns the current content of the selected entry.
	 *
	 * @param key          the entry name
	 * @param defaultValue a default value if the entry does not exist
	 * @return the current content or {@code defaultValue} if entry does not exist
	 */
	default boolean getAsBooleanOrDefault(@NotNull CharSequence key, boolean defaultValue) {
		return getAsBoolean(key).orElse(defaultValue);
	}

	/**
	 * Remove a multiple section entries
	 *
	 * @param keys the entries to remove
	 */
	default void removeAll(CharSequence @NotNull ... keys) {
		for (CharSequence key : keys) {
			remove(key);
		}
	}

	/**
	 * Returns the current content as {@link List} of the selected entry.
	 *
	 * @param key the entry name
	 * @return the entry content or {@link Optional#empty()} if entry not exists
	 */
	default @NotNull List<String> getAsList(@NotNull CharSequence key) {
		return getAsList(key, ",");
	}

	/**
	 * Returns the current content as {@link Set} of the selected entry.
	 *
	 * @param key the entry name
	 * @return the entry content or {@link Optional#empty()} if entry not exists
	 */
	default @NotNull Set<String> getAsSet(@NotNull CharSequence key) {
		return getAsSet(key, ",");
	}

}
