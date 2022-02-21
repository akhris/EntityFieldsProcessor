plugins {
    kotlin("jvm")
//    kotlin("kapt")
}

group = "com.github.akhris"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":annotations"))
    implementation("com.squareup:kotlinpoet:1.10.2")
    implementation("com.squareup:kotlinpoet-metadata:1.10.2")
}