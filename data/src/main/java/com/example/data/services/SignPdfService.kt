package com.example.data.services

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import com.example.data.common.CustomResult
import com.example.domain.requestModel.SignPdfRequestData
import com.example.domain.responseModel.VerifySignResponseData
import java.io.IOException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException

interface SignPdfService {
    @Throws(IOException::class,NoSuchAlgorithmException::class,InvalidKeyException::class)
    fun signPdf(signPdfRequestData: SignPdfRequestData):CustomResult<Intent>

    fun verifySign(context: Context, result: ActivityResult):CustomResult<VerifySignResponseData>
}