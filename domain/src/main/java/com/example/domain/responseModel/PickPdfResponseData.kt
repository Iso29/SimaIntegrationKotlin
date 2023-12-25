package com.example.domain.responseModel

import android.content.Intent
import java.io.File

data class PickPdfResponseData(
    val file : File?,
    val intent : Intent,
    val isEligibleToSign : Boolean=false
) {
}