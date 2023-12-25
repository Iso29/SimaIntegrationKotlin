package com.example.domain.responseModel

import android.content.Intent
import android.net.Uri

data class VerifySignResponseData(
    val documentUri : Uri,
    val intent: Intent
) {
}