package com.example.domain.responseModel

import android.content.Intent
import android.net.Uri

data class PickPdfResponseData(
    val documentUri : Uri,
    val intent : Intent,
    val isEligibleToSign : Boolean=false
) {
}