dependencies {
    api(libs.jackson.annotations)
    api(project(":axochat4j-core"))
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}