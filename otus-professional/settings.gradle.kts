rootProject.name = "otus-professional"
include("hw01-gradle")
include("hw02-collections")
include("hw03-annotations")
include("hw04-gc")
include("hw05-aop")
include("hw06-solid")
include("hw07-patterns")
include("hw08-serialization")
include("hw09-jdbc:demo")
include("hw09-jdbc:homework")
include("hw10-cache")
include("hw11-jpql")
include("hw12-webServer")
include("hw13-di")
include("hw14-springDataJdbc")
include("hw15-threads")
include("hw16-cuncurrentCollections")
include("hw17-multiprocess")
include("hw18-webflux-chat:client-service")
include("hw18-webflux-chat:datastore-service")

pluginManagement {
    val dependencyManagement: String by settings
    val springframeworkBoot: String by settings
    val johnrengelmanShadow: String by settings
    val sonarlint: String by settings
    val spotless: String by settings
    val protobufVer: String by settings

    plugins {
        id("io.spring.dependency-management") version dependencyManagement
        id("org.springframework.boot") version springframeworkBoot
        id("com.github.johnrengelman.shadow") version johnrengelmanShadow
        id("name.remal.sonarlint") version sonarlint
        id("com.diffplug.spotless") version spotless
        id("com.google.protobuf") version protobufVer
    }
}
