dependencies {
    compileOnly(libs.jetbrains.annotations)
    api(libs.netty.handler)
    api(libs.netty.codec.http)
    api(project(":axochat4j-core"))
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}