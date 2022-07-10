package com.github.ushiosan23.simple_ini.section.advanced;

import com.github.ushiosan23.simple_ini.section.Section;
import org.jetbrains.annotations.NotNull;

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
	 * @return Returns the current section name
	 * @throws UnsupportedOperationException This element not support this method
	 */
	@Override
	default @NotNull String getName() {
		throw new UnsupportedOperationException();
	}

}
