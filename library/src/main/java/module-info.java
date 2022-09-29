/**
 * Module used for handling .ini files and also the use of their information in a
 * virtual way for handling and saving.
 * <p>
 * It has an advanced mode that supports attributes and multiple line entries
 * (not recommended if only common use of ini files is desired).
 * Also, if you wish you can add functionality thanks to the interfaces that are available.
 */
module com.github.ushiosan.simple_ini {
	requires com.github.ushiosan.jvm_utilities;
	requires static org.jetbrains.annotations;

	exports ushiosan.simple_ini;
	exports ushiosan.simple_ini.conversion;
	exports ushiosan.simple_ini.section;
}