rootProject.name = "otus-professional"
include("hw01-gradle")
include("hw02-collections")
include("hw03-annotations")
include("hw04-gc")
include("hw05-aop")
include("hw06-solid")

pluginManagement {
    val dependencyManagement: String by settings
    val springframeworkBoot: String by settings
    val johnrengelmanShadow: String by settings
    val sonarlint: String by settings
    val spotless: String by settings

    plugins {
        id("io.spring.dependency-management") version dependencyManagement
        id("org.springframework.boot") version springframeworkBoot
        id("com.github.johnrengelman.shadow") version johnrengelmanShadow
        id("name.remal.sonarlint") version sonarlint
        id("com.diffplug.spotless") version spotless
    }
}
