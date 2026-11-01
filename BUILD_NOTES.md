# Build system requirements

- Use a modern, supported Gradle wrapper and RetroFuturaGradle 2.x rather than legacy ForgeGradle.
- Target Minecraft 1.7.10 and Forge 10.13.4.1614.
- Compile mod classes to Java 8-compatible bytecode; do not confuse build JDK with target bytecode.
- Declare Baubles as a compile/runtime dependency as appropriate; optional magic integrations must not be hard runtime dependencies.
- Commit wrapper scripts and wrapper JAR along with build.gradle, settings.gradle, gradle.properties and source once validated.
- Pin tested Gradle, RetroFuturaGradle and JDK versions together; do not assume a Gradle upgrade is compatible with every RFG release.
- Validate `gradlew build` and resulting JAR in a clean Forge 1.7.10 test instance before calling the setup complete.

## Known compatibility hazard

A previous BigTrees build encountered Gradle instrumentation errors involving RetroFuturaGradle 2.0.2 and class major version 69 under a Java 25/26 toolchain. For FragileTalismans, select and test a supported build JDK (likely JDK 21) rather than relying on the host's newest JDK.

## Status
Requirements recorded; build files and wrapper not yet committed or verified.
