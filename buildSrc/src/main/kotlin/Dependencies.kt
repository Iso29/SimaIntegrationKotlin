import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project

object Dependencies {
    const val core_ktx = "androidx.core:core-ktx:${Versions.core_ktx_ver}"
    const val androidx_appCompact =  "androidx.appcompat:appcompat:${Versions.appcompat_ver}"
    const val constraintlayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintlayout_ver}"
    const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle_ver}"
    const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle_ver}}"
    const val material_component = "com.google.android.material:material:${Versions.material_ver}"

    const val nav_ui = "androidx.navigation:navigation-ui-ktx:${Versions.nav_ver}"
    const val nav_fragment = "androidx.navigation:navigation-fragment-ktx:${Versions.nav_ver}"

    const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin_ver}"
    const val coroutines_core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines_ver}"
    const val coroutines_android ="org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinCoroutines_ver}"
    const val kotlinx_serialization ="org.jetbrains.kotlinx:kotlinx-serialization-core:${Versions.kotlinx_serialization_ver}"

    const val dagger_common = "com.google.dagger:dagger:${Versions.dagger_ver}"
    const val dagger_compiler = "com.google.dagger:dagger-compiler:${Versions.dagger_ver}"

    const val test_ext = "androidx.test.ext:junit:${Versions.test_ext_ver}"
    const val test_espresso = "androidx.test.espresso:espresso-core:${Versions.test_espresso_ver}"
    const val junit = "junit:junit:${Versions.junit_ver}"

    const val guava = "com.google.guava:guava:${Versions.guava_ver}"
}

fun DependencyHandler.dataModule(){
    implementation(project(":data"))
}

fun DependencyHandler.guava(){
    implementation(Dependencies.guava)
}

fun DependencyHandler.domainModule(){
    implementation(project(":domain"))
}

fun DependencyHandler.navigation(){
    implementation(Dependencies.nav_ui)
    implementation(Dependencies.nav_fragment)
}

fun DependencyHandler.androidx(){
    implementation(Dependencies.material_component)
    implementation(Dependencies.androidx_appCompact)
    implementation(Dependencies.constraintlayout)
    implementation(Dependencies.core_ktx)
    implementation(Dependencies.viewModel)
    implementation(Dependencies.liveData)
}

fun DependencyHandler.jetBrains(){
    implementation(Dependencies.stdlib)
    implementation(Dependencies.coroutines_core)
    implementation(Dependencies.coroutines_android)
    implementation(Dependencies.kotlinx_serialization)
}

fun DependencyHandler.dagger(){
    implementation(Dependencies.dagger_common)
    kapt(Dependencies.dagger_compiler)
}

fun DependencyHandler.allTest(){
    testImplementation(Dependencies.junit)
    androidTestImpl(Dependencies.test_ext)
    androidTestImpl(Dependencies.test_espresso)
}