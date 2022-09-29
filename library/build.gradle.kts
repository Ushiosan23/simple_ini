import defined.SimpleIniProject

plugins {
	id("common-java-library")
	id("common-maven-publishing")
}

SimpleIniProject.configureAll(project)