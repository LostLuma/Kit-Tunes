plugins {
    id("kit_tunes.java.08")
    id("kit_tunes.base")
    id("kit_tunes.default_resources")
}

dependencies {
    implementation(project(":kit-tunes-api"))

    implementation(libs.annotations)
    implementation(libs.quilt.loader)
    implementation(libs.gson)
    implementation(libs.slf4j)
}
