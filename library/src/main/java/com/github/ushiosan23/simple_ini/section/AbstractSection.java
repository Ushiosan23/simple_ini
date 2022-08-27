package com.github.ushiosan23.simple_ini.section;

import com.github.ushiosan23.jvm.collections.Containers;
import com.github.ushiosan23.jvm.functions.apply.IApply;
import com.github.ushiosan23.simple_ini.conversion.BooleanConverter;
import com.github.ushiosan23.simple_ini.conversion.CollectionConverter;
import com.github.ushiosan23.simple_ini.conversion.NumberConverter;
import com.github.ushiosan23.simple_ini.utilities.SectionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Base object to create new implementations of the {@link Section} objects. It contains a series of default methods
 * (that you can modify without any problem) that take care of the basic behavior of the sections.
 */
public abstract class AbstractSection implements Section {

	/* -----------------------------------------------------
	 * Properties
	 * ----------------------------------------------------- */

	/**
	 * Current section name
	 */
	private String name;

	/**
	 * Current default section
	 */
	private Section defaultSection;

	/**
	 * All entries content
	 */
	private final Map<String, String> entryContainer;

	/* -----------------------------------------------------
	 * Constructors
	 * ----------------------------------------------------- */

	/**
	 * Primary constructor. This constructor defines the section name and the
	 * default section object (can be {@code null}).
	 *
	 * @param sectionName The section name
	 * @param section     The default section (can be {@code null})
	 */
	public AbstractSection(@NotNull CharSequence sectionName, @Nullable Section section) {
		entryContainer = Containers.mutableMapOf();
		defaultSection = section;
		name = SectionUtils.getValidName(sectionName);
	}

	/**
	 * Secondary constructor. This constructor defines only the section name and the
	 * default section always is {@code null}.
	 *
	 * @param sectionName The section name
	 */
	public AbstractSection(@NotNull CharSequence sectionName) {
		this(sectionName, null);
	}

	/* -----------------------------------------------------
	 * Methods
	 * ----------------------------------------------------- */

	/**
	 * Rename the section
	 *
	 * @param newName The name of the section
	 */
	@Override
	public void setName(@NotNull CharSequence newName) {
		name = SectionUtils.getValidName(newName);
	}

	/**
	 * Insert a new entry in the section.
	 *
	 * @param key   The entry name
	 * @param value The entry value
	 * @return Returns the last value of the entry if it already existed or {@link Optional#empty()} otherwise.
	 */
	@Override
	public @NotNull Optional<String> put(@NotNull CharSequence key, @Nullable CharSequence value) {
		String realKey = SectionUtils.getValidName(key);
		String realValue = value == null ? null : value.toString();

		// Blank keys are not valid
		if (realKey.isBlank()) return Optional.empty();
		return Optional.ofNullable(entryContainer.put(realKey, realValue));
	}

	/**
	 * Insert a multiple entries in the section.
	 *
	 * @param elements The entries to insert
	 * @see #put(CharSequence, CharSequence)
	 */
	@Override
	public void putAll(@NotNull Collection<Map.Entry<String, String>> elements) {
		for (Map.Entry<String, String> item : elements) {
			put(item.getKey(), item.getValue());
		}
	}

	/**
	 * Insert a multiple entries in the section.
	 *
	 * @param elements The entries to insert
	 * @see #put(CharSequence, CharSequence)
	 */
	@SafeVarargs
	@Override
	public final void putAll(Map.Entry<String, String> @NotNull ... elements) {
		putAll(List.of(elements));
	}

	/**
	 * Remove a section entry
	 *
	 * @param key The entry to remove
	 * @return Returns the last value of the entry if it already existed {@link Optional#empty()} otherwise.
	 */
	@Override
	public Optional<String> remove(@NotNull CharSequence key) {
		return Optional.ofNullable(entryContainer.remove(key.toString()));
	}

	/**
	 * Remove all entries
	 */
	@Override
	public void clear() {
		entryContainer.clear();
	}

	/**
	 * Set a default session
	 *
	 * @param section The default section object
	 * @return Returns the last value of the entry if it already existed or {@link Optional#empty()} otherwise.
	 */
	@Override
	public @NotNull Optional<Section> setDefaultSection(@Nullable Section section) {
		Optional<Section> oldValue = getDefaultSection();
		// Change value
		defaultSection = section;
		return oldValue;
	}

	/**
	 * Get the current section name
	 *
	 * @return Returns the current section name
	 */
	@Override
	public @NotNull String getName() {
		return name;
	}

	/**
	 * Returns the default section to search for if it does not exist in the current section.
	 *
	 * @return Returns the default section or {@link Optional#empty()} if not defined
	 */
	@Override
	public @NotNull Optional<Section> getDefaultSection() {
		return Optional.ofNullable(defaultSection);
	}

	/**
	 * Returns size of entries that the current session has
	 *
	 * @return Returns the size of entries
	 */
	@Override
	public int size() {
		return entryContainer.size();
	}

	/**
	 * Determines if the current session does not contain any entries.
	 *
	 * @return Returns {@code true} if the section is empty or {@code false} otherwise
	 */
	@Override
	public boolean isEmpty() {
		return entryContainer.isEmpty();
	}

	/**
	 * Method to check if an entry exists or not.
	 *
	 * @param key The entry name
	 * @return Returns {@code true} if the entry exists or {@code false} otherwise
	 */
	@Override
	public boolean containsKey(@NotNull CharSequence key) {
		return entryContainer.containsKey(key.toString());
	}

	/**
	 * Returns the current content of the selected entry.
	 *
	 * @param key The entry name
	 * @return Returns the entry content or {@link Optional#empty()} if entry not exists
	 */
	@Override
	public @NotNull Optional<String> get(@NotNull CharSequence key) {
		return Optional.ofNullable(entryContainer.get(key.toString()));
	}

	/**
	 * Returns the current content of the selected entry.
	 *
	 * @param key The entry name
	 * @return Returns the entry content or {@link Optional#empty()} if entry not exists
	 */
	@Override
	public @NotNull Optional<Number> getAsNumber(@NotNull CharSequence key) {
		return customGetAs(key, NumberConverter::toNumber);
	}

	/**
	 * Returns the current content of the selected entry.
	 *
	 * @param key The entry name
	 * @return Returns the entry content or {@link Optional#empty()} if entry not exists
	 */
	@Override
	public @NotNull Optional<Boolean> getAsBoolean(@NotNull CharSequence key) {
		return customGetAs(key, BooleanConverter::toBoolean);
	}

	/**
	 * Returns the current content as {@link List} of the selected entry.
	 *
	 * @param key   The entry name
	 * @param regex The content separator
	 * @return Returns the entry content or {@link Optional#empty()} if entry not exists
	 */
	@Override
	public @NotNull List<String> getAsList(@NotNull CharSequence key, @NotNull String regex) {
		return customGetAsRaw(key, it -> CollectionConverter.toList(it, regex));
	}

	/**
	 * Returns the current content as {@link Set} of the selected entry.
	 *
	 * @param key   The entry name
	 * @param regex The content separator
	 * @return Returns the entry content or {@link Optional#empty()} if entry not exists
	 */
	@Override
	public @NotNull Set<String> getAsSet(@NotNull CharSequence key, @NotNull String regex) {
		return customGetAsRaw(key, it -> CollectionConverter.toSet(it, regex));
	}

	/**
	 * Returns all entry names in the current section.
	 *
	 * @return Returns a {@link Set} with all entry names
	 */
	@Override
	public @NotNull Set<String> keys() {
		return entryContainer.keySet();
	}

	/**
	 * Returns all entry values in the current section.
	 *
	 * @return Returns a {@link Set} with all entry values
	 */
	@Override
	public @NotNull Set<String> values() {
		return Set.copyOf(entryContainer.values());
	}

	/**
	 * Returns all entries in the current section.
	 *
	 * @return Returns a {@link Set} with all entries
	 */
	@Override
	public @NotNull Set<Map.Entry<String, String>> entrySet() {
		return entryContainer.entrySet();
	}

	/* -----------------------------------------------------
	 * Internal methods
	 * ----------------------------------------------------- */

	/**
	 * Returns a text transformed to a specific object.
	 * If the text does not conform to the content rules then a {@code null} object is returned instead.
	 *
	 * @param key   The key to check
	 * @param apply Action transformation
	 * @param <T>   Generic return type
	 * @return Returns a transformed value or {@link Optional#empty()} is value is not valid
	 */
	protected <T> Optional<T> customGetAs(
		@NotNull CharSequence key,
		@NotNull IApply.WithResult<String, Optional<T>> apply
	) {
		Optional<String> result = get(key);
		if (result.isPresent()) {
			return apply.invoke(result.get());
		} else {
			return Optional.empty();
		}
	}

	/**
	 * Returns a text transformed to a specific object.
	 * If the text does not conform to the content rules then a {@code null} object is returned instead.
	 *
	 * @param key   The key to check
	 * @param apply Action transformation
	 * @param <T>   Generic return type
	 * @return Returns a transformed value without checking if exists
	 */
	protected <T> T customGetAsRaw(
		@NotNull CharSequence key,
		@NotNull IApply.WithResult<String, T> apply
	) {
		Optional<String> result = get(key);
		return apply.invoke(result.orElse(""));
	}

}
