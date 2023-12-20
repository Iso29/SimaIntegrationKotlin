package com.example.data.launchers

import androidx.activity.result.ActivityResultLauncher

sealed class Launcher<ActivityResultLauncher> {
    data class PickPdfResultLauncher<ActivityResultLauncher>(val launcher : ActivityResultLauncher) : Launcher<ActivityResultLauncher>()
    data class CreatePDFResultLauncher<ActivityResultLauncher>(val launcher : ActivityResultLauncher) : Launcher<ActivityResultLauncher>()
    data class SignPdfResultLauncher<ActivityResultLauncher>(val launcher : ActivityResultLauncher) : Launcher<ActivityResultLauncher>()
    data class SignChallengeResultLauncher<ActivityResultLauncher>(val launcher : ActivityResultLauncher) : Launcher<ActivityResultLauncher>()
}