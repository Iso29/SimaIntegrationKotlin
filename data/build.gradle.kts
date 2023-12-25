plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

apply<MainGradlePlugin>()

android {
    namespace = "com.example.data"
}

dependencies {
    androidx()
    jetBrains()
    domainModule()
    spongycastle()
    pdfbox()
    implementation("org.apache.commons:commons-io:1.3.2")
}
