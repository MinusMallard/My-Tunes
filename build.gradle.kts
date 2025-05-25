// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.7.3" apply false
    alias(libs.plugins.compose.compiler) apply false
    id("com.android.test") version "8.5.2" apply false
}
