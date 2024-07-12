plugins {
    id("kit_tunes.java.17")
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

    modImplementation(libs.qsl.resource.loader)

    modImplementation("com.terraformersmc:modmenu:${project.property("modmenu_version")}")
}
