plugins {
    id("io.papermc.paperweight.userdev") version "1.5.9"

    `java-library`
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

val mcVersion = providers.gradleProperty("mcVersion").get()

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

    val jarsDir = File("$rootDir/jars")

    assemble {
        if (jarsDir.exists()) jarsDir.delete() else jarsDir.mkdirs()

        dependsOn(reobfJar)
    }

    reobfJar {
        outputJar.set(file("$jarsDir/${rootProject.name}-${rootProject.version}.jar"))
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