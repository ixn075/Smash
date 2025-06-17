plugins {
    java
    `maven-publish`
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public")
    //maven("https://eldonexus.de/repository/maven-public") // sadu
    //maven("https://libraries.minecraft.net")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.5-R0.1-SNAPSHOT")
    compileOnly("com.google.code.gson:gson:2.11.0")
    //implementation("de.chojo.sadu:sadu-mariadb:2.3.0")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

group = "de.ixn075"
version = "1.0"
description = "The plugin for Super Smash Bros but in Minecraft."

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
