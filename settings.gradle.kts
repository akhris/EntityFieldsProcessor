pluginManagement{
    plugins {
        kotlin("jvm") version "1.6.10"
        kotlin("kapt") version "1.6.10"
        application
    }
}
rootProject.name = "fieldsannotation"
include("annotations")
include("processor")
