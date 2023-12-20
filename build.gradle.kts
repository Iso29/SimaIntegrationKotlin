// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.nav_ver}")
    }
}


plugins {
    id("com.google.dagger.hilt.android") version Versions.hilt_ver apply false
//    id("com.google.dagger.hilt.android") version "2.44" apply false
//    id("com.android.application") version "8.1.4" apply false
//    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
//    id("com.android.library") version "8.1.4" apply false
}
