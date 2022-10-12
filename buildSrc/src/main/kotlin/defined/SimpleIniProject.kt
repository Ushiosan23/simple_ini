package defined

import defined.common.dependencyOf
import defined.common.developersOf
import defined.common.javadocLinksOf
import defined.common.licensesOf
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.tasks.TaskContainer
import project.SimpleJavaProject
import project.TypeDependency
import publishing.*

val SimpleIniProject.releasePublication
	get() = PublicationInfo(
		name = "release",
		version = "0.2.1",
		pom = PublicationPom(
			artifactUrl = "https://github.com/Ushiosan23/simple_ini",
			description = "Library for managing ini files",
			licenses = licensesOf(),
			developers = developersOf(),
			scm = ScmConnection()
		)
	)

object SimpleIniProject : SimpleJavaProject, SimpleMavenProject {
	private lateinit var projectImpl: Project

	/* -----------------------------------------------------
	 * Properties
	 * ----------------------------------------------------- */

	/**
	 * Project name
	 */
	override val projectName: String
		get() = "Simple INI"

	/**
	 * project group id
	 */
	override val projectGroup: String
		get() = "com.github.ushiosan23"

	/**
	 * project artifact name
	 */
	override val artifactId: String
		get() = "simple-ini"

	/**
	 * project version string
	 */
	override val artifactVersion: String?
		get() = releasePublication.version

	/**
	 * All maven publications
	 */
	override val registeredPublications: List<PublicationInfo>
		get() = listOf(releasePublication)

	/**
	 * project javadoc information
	 */
	override val javadocInfo: SimpleJavaProject.JavadocInfo
		get() = SimpleJavaProject.JavadocInfo(
			urls = javadocLinksOf(
				"https://javadoc.io/doc/com.github.ushiosan23/jvm-utilities/latest"
			)
		)

	/**
	 * Generate automatically extra maven tasks
	 */
	override val autoGenerateMavenTasks: Boolean
		get() = true

	/**
	 * Signing information
	 */
	override val signingInfo: SigningInfo
		get() = SigningInfo(
			getEnv("SIGNING_KEY_ID", ""),
			getEnv("SIGNING_PASSWORD", ""),
			getEnv("SIGNING_PGP_B64", "")
		)

	/* -----------------------------------------------------
	 * Methods
	 * ----------------------------------------------------- */

	/**
	 * project dependency map
	 *
	 * @return All dependency map
	 */
	override fun dependencyMap(): Map<Any, List<*>?> = dependencyOf(
		TypeDependency.IMPLEMENTATION to listOf<Any>(
			"com.github.ushiosan23:jvm-utilities:0.2.3"
		)
	)

	/**
	 * Returns the current project
	 */
	override fun currentProject(): Project =
		projectImpl

	/**
	 * Define current project
	 */
	override fun setCurrentProject(project: Project) {
		projectImpl = project
	}

	/**
	 * Abstract project configuration
	 */
	override fun configureProject() = Unit

	/**
	 * Configure all plugins per project
	 *
	 * @param project current project
	 */
	override fun configurePluginsProject(plugins: PluginContainer, extensions: ExtensionContainer) {
		super<SimpleJavaProject>.configurePluginsProject(plugins, extensions)
		super<SimpleMavenProject>.configurePluginsProject(plugins, extensions)
	}

	/**
	 * Configure all task per project
	 *
	 * @param project current project
	 */
	override fun configureTaskProject(tasks: TaskContainer) {
		super<SimpleJavaProject>.configureTaskProject(tasks)
		super<SimpleMavenProject>.configureTaskProject(tasks)
	}

}