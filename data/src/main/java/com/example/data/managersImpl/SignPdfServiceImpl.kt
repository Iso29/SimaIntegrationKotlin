package com.example.data.managersImpl

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import com.example.data.common.CustomResult
import com.example.data.services.SignPdfService
import com.example.data.utils.CallErrors
import com.example.domain.keys.FieldKeys
import com.example.domain.requestModel.SignPdfRequestData
import com.example.domain.responseModel.VerifySignResponseData
import com.tom_roush.pdfbox.io.IOUtils
import com.tom_roush.pdfbox.pdmodel.PDDocument
import org.spongycastle.cert.X509CertificateHolder
import org.spongycastle.cms.CMSProcessable
import org.spongycastle.cms.CMSProcessableByteArray
import org.spongycastle.cms.CMSSignedData
import org.spongycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder
import org.spongycastle.util.Selector
import org.spongycastle.util.Store
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.security.MessageDigest
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class SignPdfServiceImpl() : SignPdfService {
    override fun signPdf(signPdfRequestData: SignPdfRequestData): CustomResult<Intent> {
        val stream: InputStream? = signPdfRequestData.context.contentResolver.openInputStream(signPdfRequestData.documentUri)
        val documentBytes = IOUtils.toByteArray(stream)

        val md = MessageDigest.getInstance(signPdfRequestData.clientHashAlgorithm)
        md.update(documentBytes)
        val documentHash = md.digest()

        val mac = Mac.getInstance(signPdfRequestData.clientSignatureAlgorithm)
        mac.init(
            SecretKeySpec(
                signPdfRequestData.clientMasterKey.toByteArray(),
                signPdfRequestData.clientSignatureAlgorithm
            )
        )
        val documentSignature = mac.doFinal(documentHash)

        signPdfRequestData.intent = signPdfRequestData.intent.setAction(signPdfRequestData.operationTypes.op)
            .setFlags(0)
            .setData(signPdfRequestData.documentUri)
            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            .putExtra(FieldKeys.EXTRA_SERVICE_FIELD, signPdfRequestData.extraServiceValue)
            .putExtra(FieldKeys.EXTRA_CLIENT_ID_FIELD, signPdfRequestData.extraClientIdValue)
            .putExtra(FieldKeys.EXTRA_SIGNATURE_FIELD, documentSignature)
            .putExtra(FieldKeys.EXTRA_LOGO_FIELD, signPdfRequestData.logo)
            .putExtra(FieldKeys.EXTRA_USER_CODE_FIELD, signPdfRequestData.fin)
            .putExtra(FieldKeys.EXTRA_REQUEST_ID_FIELD, signPdfRequestData.uuid)

        return CustomResult.Success(signPdfRequestData.intent)
    }

    override fun verifySign(context: Context,result: ActivityResult): CustomResult<VerifySignResponseData> {
        try {
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data ?: return CustomResult.Error(CallErrors.ErrorEmptyResponse)
                val status = intent.getStringExtra("status")
                val message = intent.getStringExtra("message")
                if (status == null || status != "success") {
                    return CustomResult.Error(CallErrors.ErrorWithMessage(message?:""))
                }
                val documentUri = intent.data
                val documentFile = File.createTempFile("temp", ".pdf")
                val stream: InputStream? = documentUri?.let {
                    context.contentResolver.openInputStream(
                        it
                    )
                }
                val bytes = IOUtils.toByteArray(stream)
                org.apache.commons.io.FileUtils.writeByteArrayToFile(documentFile, bytes)
                val pdDocument = PDDocument.load(documentFile)
                for (sig in pdDocument.signatureDictionaries) {
                    val signatureContent = sig.getContents(FileInputStream(documentFile))
                    val signedContent = sig.getSignedContent(FileInputStream(documentFile))
                    val cmsProcessableInputStream: CMSProcessable =
                        CMSProcessableByteArray(signedContent)
                    val signedData = CMSSignedData(cmsProcessableInputStream, signatureContent)
                    val certificatesStore  : Store<X509CertificateHolder> = signedData.certificates
                    if (certificatesStore.getMatches(null).isEmpty()) {
                        return CustomResult.Error(CallErrors.ErrorNoCertifate)
                    }
                    val signers = signedData.signerInfos.signers
                    if (signers.isEmpty()) {
                        return CustomResult.Error(CallErrors.ErrorNoSigner)
                    }
                    val signerInformation = signers.iterator().next()
                    val matches = certificatesStore.getMatches(signerInformation.sid as Selector<X509CertificateHolder>)
                    if (matches.isEmpty()) {
                        return CustomResult.Error(CallErrors.ErrorDoesNotMatchCertificates(signerInformation))

                    }
                    val certificateHolder = matches.iterator().next()
                    val verifier = JcaSimpleSignerInfoVerifierBuilder().build(certificateHolder)
                    if (!signerInformation.verify(verifier)) {
                        return CustomResult.Error(CallErrors.ErrorVerificationError)
                    }
                }
                val intentDirectory =
                    Intent(Intent.ACTION_CREATE_DOCUMENT).addCategory(Intent.CATEGORY_OPENABLE)
                        .setType("application/pdf").putExtra(Intent.EXTRA_TITLE, "signed.pdf")
                return CustomResult.Success(VerifySignResponseData(documentUri!!,intentDirectory))
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                return CustomResult.Error(CallErrors.ErrorOperationCancelled)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return CustomResult.Error(CallErrors.ErrorParsing)
        }
        return CustomResult.Error(CallErrors.ErrorWithMessage(""))
    }
}