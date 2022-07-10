/**
 * Module used for handling .ini files and also the use of their information in a
 * virtual way for handling and saving.
 * <p>
 * It has an advanced mode that supports attributes and multiple line entries
 * (not recommended if only common use of ini files is desired).
 * Also, if you wish you can add functionality thanks to the interfaces that are available.
 */
@SuppressWarnings("JavaModuleNaming")
module com.github.ushiosan23.simple_ini {
	requires static org.jetbrains.annotations;
	requires static com.github.ushiosan23.jvm;

	exports com.github.ushiosan23.simple_ini;
	exports com.github.ushiosan23.simple_ini.section;
	exports com.github.ushiosan23.simple_ini.section.data;
	exports com.github.ushiosan23.simple_ini.section.advanced;
	exports com.github.ushiosan23.simple_ini.utilities;
}