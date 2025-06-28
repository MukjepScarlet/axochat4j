plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    api(project(":axochat4j-core"))

    api(libs.okhttp)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

kotlin {
    jvmToolchain(8)
}