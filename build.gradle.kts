plugins {
    id("java")
    id("fabric-loom") version "1.1-SNAPSHOT"
    id("maven-publish")
}

group = "dev.stashy"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val archives_base_name: String by project.properties
val minecraft_version: String by project.properties
val yarn_mappings: String by project.properties
val loader_version: String by project.properties
val minimal_json_version: String by project.properties

dependencies {
    minecraft("com.mojang:minecraft:${minecraft_version}")
    mappings("net.fabricmc:yarn:${yarn_mappings}:v2")
    modImplementation("net.fabricmc:fabric-loader:${loader_version}")

    include(implementation("com.eclipsesource.minimal-json:minimal-json:${minimal_json_version}")!!)

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

base {
    archivesName.set(archives_base_name)
}

java {
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks {
    withType<ProcessResources>() {
        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand("version" to project.version)
        }
    }

    withType<JavaCompile>().configureEach {
        options.release.set(17)
    }

    jar {
        from("LICENSE") {
            rename { "${it}_${base.archivesName.get()}" }
        }
    }
}

val publish_username: String? by project
val publish_password: String? by project

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }

    repositories {
        mavenLocal()
        maven("https://repo.stashy.dev/snapshots") {
            credentials {
                username = publish_username
                password = publish_password
            }
        }
    }
}


tasks.test {
    useJUnitPlatform()
}
