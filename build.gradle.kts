allprojects {
    group = "moe.lasoleil"
    version = "0.1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    plugins.apply("java-library")
    plugins.apply("maven-publish")

    afterEvaluate {
        extensions.findByType<PublishingExtension>()?.apply {
            publications {
                create<MavenPublication>("mavenJava") {
                    from(components.findByName("java"))
                    groupId = project.group.toString()
                    artifactId = project.name
                    version = project.version.toString()
                }
            }
            repositories {
                mavenLocal()
            }
        }
    }
}
