
plugins {
    kotlin("jvm")
    kotlin("kapt")
}

group = "com.programistich"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(fileTree("libs"))
    implementation(project(":annotations"))
    kapt(project(":compiler"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
