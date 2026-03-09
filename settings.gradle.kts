// settings.gradle.kts
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

// Enable automatic JDK provisioning
javaToolchains {
    // Gradle will fetch a JDK 25 from the internet when needed
    languageVersion.set(JavaLanguageVersion.of(25))
}

