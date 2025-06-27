dependencies {
    annotationProcessor(libs.lombok)
    compileOnly(libs.lombok)

    compileOnly(libs.jetbrains.annotations)
    api(libs.jackson.databind)
    api(project(":axochat4j-core"))
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}