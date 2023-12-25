package com.example.data.managersImpl

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.result.ActivityResult
import com.example.data.common.CustomResult
import com.example.data.services.PickPDfService
import com.example.data.utils.CallErrors
import com.example.data.utils.PermissionUtils
import com.example.domain.keys.FieldKeys
import com.example.domain.responseModel.PickPdfResponseData
import com.tom_roush.pdfbox.io.IOUtils
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.pdmodel.PDPage
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream
import com.tom_roush.pdfbox.pdmodel.font.PDFont
import com.tom_roush.pdfbox.pdmodel.font.PDType1Font
import java.io.File
import java.io.InputStream
import java.io.OutputStream


class PickPdfServiceImpl() : PickPDfService {
    override fun pickSignPDF(activity: Activity, onComplete : PermissionUtils.PermissionResultListener){
        PermissionUtils.startPermissionRequest(
            activity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            onComplete
        )
    }

    override fun pickDirectoryResult(result: ActivityResult,context: Context,fileToSave : Uri): CustomResult<String> {
        try {
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data ?: return CustomResult.Error(CallErrors.ErrorNoDirectoryPicked)
                val out: OutputStream? = intent.data?.let {
                    context.contentResolver.openOutputStream(
                        it
                    )
                }
                val `in`: InputStream? = context.contentResolver.openInputStream(fileToSave)
                if (out == null || `in` == null) {
                    return CustomResult.Error(CallErrors.ErrorSavingFile)
                }
                IOUtils.copy(`in`, out)
                `in`.close()
                out.close()
                return CustomResult.Success("File successfully signed")
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                return CustomResult.Error(CallErrors.ErrorNoDirectoryPicked)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            return CustomResult.Error(CallErrors.ErrorSavingFile)
        }
        return CustomResult.Error(CallErrors.ErrorWithMessage(""))
    }

    override fun startPickIntent(): CustomResult<Intent> {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).setType("application/pdf")
        return if(intent!=null) {
            CustomResult.Success(Intent(Intent.ACTION_OPEN_DOCUMENT).setType("application/pdf"))
        }else{
            CustomResult.Error(CallErrors.ErrorIntent)}
    }

    override fun createSignPdf(context: Context): CustomResult<PickPdfResponseData> {
        try {
            //NEW_DEMO: HERE INTENT INITIALIZATION HAS BEEN CHANGED
            var intent = Intent().setPackage(FieldKeys.PACKAGE_NAME)
            val packageManager: PackageManager = context.packageManager
            val resolveInfoList = packageManager.queryIntentActivities(
                intent, 0
            )
            if (resolveInfoList.isEmpty()) {
                intent = try {
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + FieldKeys.PACKAGE_NAME)
                    )
                } catch (e: Exception) {
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" + FieldKeys.PACKAGE_NAME)
                    )
                }
                return CustomResult.Success(PickPdfResponseData(null,intent,false))
            } else {
                val file: File = File(context.filesDir, "test.pdf")
                if (!file.exists()) {
                    createPdf(context.filesDir.toString() + "/test.pdf")
                }
                return CustomResult.Success(PickPdfResponseData(file,intent,true))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return CustomResult.Error(CallErrors.ErrorCreatePDF)
        }
    }

    override fun createPdf(path: String) {
        val document = PDDocument()
        val page = PDPage()
        document.addPage(page)

        val font: PDFont = PDType1Font.HELVETICA_BOLD

        val contentStream = PDPageContentStream(document, page)

        contentStream.beginText()
        contentStream.setFont(font, 12f)
        contentStream.newLineAtOffset(100f, 700f)
        contentStream.showText("Test PDF")
        contentStream.endText()

        contentStream.close()

        document.save(path)
        document.close()
    }

}