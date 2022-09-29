package ushiosan.simple_ini.section.advanced;

import org.jetbrains.annotations.NotNull;

import ushiosan.simple_ini.section.Section;

/**
 * Interface used for the representation of attributes of the sections.
 * <p>
 * Since they are very similar to sections then they are used as simpler sections,
 * for that reason they implement the "Section" interface.
 */
public interface SectionAttributes extends Section {

	/**
	 * Get the current section name
	 *
	 * @return the current section name
	 * @throws UnsupportedOperationException this element not support this method
	 */
	@Override
	default @NotNull String getName() {
		throw new UnsupportedOperationException();
	}

}
