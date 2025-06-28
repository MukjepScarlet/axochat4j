dependencies {
    compileOnly(libs.jetbrains.annotations)
    api(project(":axochat4j-core"))
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}