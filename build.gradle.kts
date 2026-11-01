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
    // Optional at runtime; copy Thaumcraft-1.7.10-4.2.3.5.jar to libs for compiling.
    val thaumcraftJars = fileTree("libs") { include("Thaumcraft-1.7.10-4.2.3.5.jar") }.files.toList()
    require(thaumcraftJars.size == 1) {
        "Thaumcraft integration requires libs/Thaumcraft-1.7.10-4.2.3.5.jar"
    }
    compileOnly(rfg.deobf(files(thaumcraftJars.single())))
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}
