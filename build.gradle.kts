plugins {
    //trick: for the same plugin versions in all sub-modules
    alias(libs.plugins.androidApplication).apply(false)
    alias(libs.plugins.androidLibrary).apply(false)
    alias(libs.plugins.kotlinAndroid).apply(false)
    alias(libs.plugins.kotlinMultiplatform).apply(false)
    alias(libs.plugins.compose.compiler).apply(false)
    alias(libs.plugins.org.jetbrains.kotlin.plugin.serialization).apply(false)
    alias(libs.plugins.sqlDelight).apply(false)
    id("co.touchlab.skie") version "0.8.4" apply false
}
