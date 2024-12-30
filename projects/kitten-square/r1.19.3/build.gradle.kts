plugins {
    id("kit_tunes.java.17")
    id("kit_tunes.modern_submodule")
}

val modmenu_version = project.property("modmenu_version") as String

mod {
    dependencies {
        required("quilt_resource_loader")
        optional("modmenu").versionAbove(modmenu_version)
    }

    entrypoint("modmenu", "net.pixaurora.kitten_square.impl.compat.ModMenuIntegration")

    intermediaryMappings = "net.fabricmc:intermediary"
    mixin("kitten_square.mixins.json")
}

repositories {
    maven {
        name = "Terraformers"
        url = uri("https://maven.terraformersmc.com/")
    }
}

dependencies {
    implementation(project(":projects:kit-tunes-api"))
    implementation(project(":projects:kitten-heart"))

    modImplementation(libs.qsl.resource.loader)

    modImplementation("com.terraformersmc:modmenu:${modmenu_version}")
    modImplementation(fabricApi.module("fabric-resource-loader-v0", project.property("fabric_api_version").toString()))
}
