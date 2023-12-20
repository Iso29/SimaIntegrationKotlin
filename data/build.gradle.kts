plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
//    kotlin("kapt")
//    id("com.google.dagger.hilt.android")
}

apply<MainGradlePlugin>()

android {
    namespace = "com.example.data"
}

dependencies {
    androidx()
    jetBrains()
//    domainModule()
//    hilt()
    spongycastle()
    pdfbox()

}
