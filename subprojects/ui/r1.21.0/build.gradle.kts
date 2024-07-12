plugins {
    id("kit_tunes.java.21")
    id("kit_tunes.module")
}

repositories {
    maven {
        name = "Terraformers"
        url = uri("https://maven.terraformersmc.com/")
    }
}

dependencies {
    implementation(project(":subprojects:api"))
    implementation(project(":subprojects:core"))

    modImplementation(fabricApi.module("fabric-resource-loader-v0", project.property("fabric_api_version").toString()))

    modImplementation("com.terraformersmc:modmenu:${project.property("modmenu_version")}")
}
