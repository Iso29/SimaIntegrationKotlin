package com.example.data.services

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResult
import com.example.data.common.CustomResult
import com.example.data.utils.PermissionUtils
import com.example.domain.responseModel.PickPdfResponseData
import java.io.IOException

interface PickPDfService {
    //
    fun startPickIntent():CustomResult<Intent>
    //
    fun pickSignPDF(activity: Activity, onComplete : PermissionUtils.PermissionResultListener)

    //
    fun createSignPdf(context : Context): CustomResult<PickPdfResponseData>
    //
    @Throws(IOException::class)
    fun createPdf(path : String)

    fun pickDirectoryResult(result: ActivityResult,context: Context,fileToSave : Uri):CustomResult<String>
}