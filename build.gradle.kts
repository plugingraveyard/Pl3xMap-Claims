plugins {
    id("io.papermc.paperweight.userdev") version "1.5.9"

    id("xyz.jpenilla.run-paper") version "2.2.3"

    `java-library`
}

base {
    archivesName.set(rootProject.name)
}

repositories {
    maven("https://oss.sonatype.org/content/repositories/snapshots/")

    maven("https://repo.glaremasters.me/repository/bloodshot")

    maven("https://repo.papermc.io/repository/maven-public/")

    maven("https://maven.enginehub.org/repo/")

    maven("https://jitpack.io")

    exclusiveContent {
        forRepository {
            maven("https://api.modrinth.com/maven/")
        }
        filter { includeGroup("maven.modrinth") }
    }
}

rootProject.version = if (System.getenv("BUILD_NUMBER") != null) "${rootProject.version}-${System.getenv("BUILD_NUMBER")}" else rootProject.version

val mcVersion = providers.gradleProperty("minecraftVersion").get()

dependencies {
    paperweight.paperDevBundle("$mcVersion-R0.1-SNAPSHOT")

    compileOnly("com.github.cjburkey01", "ClaimChunk", "0.0.22")

    compileOnly("com.griefdefender", "api", "2.1.0-SNAPSHOT")

    compileOnly("com.github.TechFortress", "GriefPrevention", "16.18")

    compileOnly("com.sk89q.worldguard", "worldguard-bukkit", "7.0.8-SNAPSHOT")

    implementation(platform("com.intellectualsites.bom:bom-1.18.x:1.28"))
    compileOnly("com.plotsquared:PlotSquared-Core")
    compileOnly("com.plotsquared:PlotSquared-Bukkit")

    compileOnly("maven.modrinth", "pl3xmap", providers.gradleProperty("pl3xmapVersion").get())
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of("17"))
}

tasks {
    javadoc {
        options.encoding = "UTF-8"
    }

    compileJava {
        options.encoding = "UTF-8"
        options.release.set(17)
    }

    runServer {
        jvmArgs("-Dnet.kyori.ansi.colorLevel=truecolor")

        defaultCharacterEncoding = Charsets.UTF_8.name()

        minecraftVersion(mcVersion)

        downloadPlugins {
            hangar("Chunky", "1.3.136")

            modrinth("pl3xmap", providers.gradleProperty("pl3xmapVersion").get())

            modrinth("griefprevention", "16.18.2")

            github("cjburkey01", "ClaimChunk", "0.0.23-RC8", "claimchunk-0.0.23-RC8-plugin.jar")

            url("https://ci.lucko.me/job/spark/401/artifact/spark-bukkit/build/libs/spark-1.10.60-bukkit.jar")

            url("https://download.luckperms.net/1532/bukkit/loader/LuckPerms-Bukkit-5.4.119.jar")

            url("https://ci.enginehub.org/repository/download/bt11/22585:id/worldguard-bukkit-7.0.10-SNAPSHOT-dist.jar?branch=version/7.0.x&guest=1")
            url("https://ci.enginehub.org/repository/download/bt10/23464:id/worldedit-bukkit-7.2.20-dist.jar?branch=version/7.2.x&guest=1")
        }
    }

    val jarsDir = File("$rootDir/jars")

    assemble {
        doFirst {
            delete(jarsDir)

            jarsDir.mkdirs()
        }

        dependsOn(reobfJar)

        doLast {
            copy {
                from(rootProject.layout.buildDirectory.files("libs/${rootProject.name}-${rootProject.version}.jar"))
                into(jarsDir)
            }
        }
    }

    processResources {
        val props = mapOf(
            "name" to rootProject.name,
            "group" to rootProject.group,
            "version" to rootProject.version,
            "description" to rootProject.description,
            "authors" to rootProject.properties["authors"],
            "website" to rootProject.properties["website"],
            "apiVersion" to rootProject.properties["apiVersion"],
        )

        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}