plugins {
    java
    alias(libs.plugins.fabric.loom)
    alias(libs.plugins.minotaur)
}

val modId = property("mod.id").toString()
val modGroup = property("mod.group").toString()
val version = property("mod.version").toString()

base.archivesName = "${modId}-${version}"

loom {
    splitEnvironmentSourceSets()

    mods.create(modId) {
        sourceSet(sourceSets.getByName("main"))
        sourceSet(sourceSets.getByName("client"))
    }
}

repositories {
    mavenCentral()

    exclusiveContent {
        forRepository {
            maven("https://maven.terraformersmc.com/") {
                name = "Terraformers"
            }
        }

        filter {
            includeGroup("com.terraformersmc")
        }
    }
}

dependencies {
    minecraft(libs.minecraft)

    implementation(libs.fabric.loader)
    implementation(libs.fabric.api)
}

tasks.processResources {
    val map = mapOf(
        "mod_id" to modId,
        "mod_version" to version,
        "fabric_loader_version" to libs.versions.fabric.loader.get(),
        "fabric_api_version" to libs.versions.fabric.api.get(),
        "minecraft_version" to libs.versions.minecraft.get(),
    )

    inputs.properties(map)
    filesMatching("fabric.mod.json") { expand(map) }
}

java {
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release = 25
}

tasks.jar { from("LICENSE") { rename { "${it}_${base.archivesName.get()}" } } }

modrinth {
    token.set(System.getenv("MODRINTH_TOKEN"))
    projectId.set("bringbackchat")
    versionNumber.set(version)
    versionType.set("release")
    uploadFile.set(tasks.jar)
    gameVersions.addAll(libs.versions.minecraft.get())
    loaders.add("fabric")
    dependencies {
        required.project("fabric-api")
    }
    syncBodyFrom = rootProject.file("README.md").readText()
}
