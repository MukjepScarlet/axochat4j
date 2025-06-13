dependencies {
    annotationProcessor(libs.lombok)
    compileOnly(libs.lombok)
    compileOnly(libs.jetbrains.annotations)

    compileOnly(libs.gson)
    compileOnly(libs.jackson.annotations)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}