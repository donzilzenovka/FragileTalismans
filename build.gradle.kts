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
    // Match BaublesExpanded regardless of its frequently changing version suffix.
    // Fail explicitly if the dependency is missing or ambiguous.
    val baublesExpandedJars = fileTree("libs") { include("BaublesExpanded*.jar") }.files.toList()
    require(baublesExpandedJars.size == 1) {
        "Expected exactly one libs/BaublesExpanded*.jar; found ${baublesExpandedJars.size}"
    }
    compileOnly(rfg.deobf(files(baublesExpandedJars.single())))
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}
