plugins {
    `java-library`
    id("com.gtnewhorizons.retrofuturagradle") version "1.4.9"
}

group = "zenovka"
version = "0.1.0"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}

minecraft {
    mcVersion.set("1.7.10")
    username.set("Developer")
}

repositories {
    mavenCentral()
    maven("https://nexus.gtnewhorizons.com/repository/public/")
}

// Baubles and optional magic APIs will be added when their exact compatible
// versions are established during source reconstruction.
dependencies {
    // Provide the installed Baubles 1.7.10 JAR locally for compilation.
    // The original Talismans 2 JAR is NOT required.
    compileOnly(rfg.deobf(files("libs/Baubles-1.7.10.jar")))
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}
