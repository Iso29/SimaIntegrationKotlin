package com.example.data.managersImpl

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResult
import com.example.data.common.CustomResult
import com.example.data.services.SignChallenge
import com.example.data.utils.CallErrors
import com.example.domain.keys.FieldKeys
import com.example.domain.requestModel.SimaParamsSignChallengeRequest
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.security.MessageDigest
import java.security.SecureRandom
import java.security.Signature
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class SignChallengeImpl : SignChallenge {
    override fun startSignChallenge(
        context: Context,
        simaParams: SimaParamsSignChallengeRequest
    ): Intent? {
        var intent: Intent? = context.packageManager.getLaunchIntentForPackage(FieldKeys.PACKAGE_NAME)
        if (intent == null) {
            Log.e("DEMO","intent  null")
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
        } else {
            Log.e("DEMO","intent not null")

            val random = SecureRandom()
            val challenge = ByteArray(64)
            random.nextBytes(challenge)
            val md = MessageDigest.getInstance(simaParams.clientHashAlgorithm)
            md.update(challenge)
            val hash = md.digest()
            val mac = Mac.getInstance(simaParams.clientSignatureAlgorithm)
            mac.init(
                SecretKeySpec(
                    simaParams.clientMasterKey.toByteArray(),
                    simaParams.clientSignatureAlgorithm
                )
            )
            val signature = mac.doFinal(hash)
            intent = intent.setAction(simaParams.operationType.op)
                .setFlags(0)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                .putExtra(FieldKeys.EXTRA_CHALLENGE_FIELD, challenge)
                .putExtra(FieldKeys.EXTRA_SERVICE_FIELD, simaParams.extraServiceValue)
                .putExtra(FieldKeys.EXTRA_CLIENT_ID_FIELD, simaParams.extraClientIdValue)
                .putExtra(FieldKeys.EXTRA_SIGNATURE_FIELD, signature)
                .putExtra(FieldKeys.EXTRA_LOGO_FIELD, simaParams.logo)
                .putExtra(FieldKeys.EXTRA_REQUEST_ID_FIELD, simaParams.uuid)
        }
        return intent
    }


    override fun handleSignIntent(result: ActivityResult, signAlgorithm: String): CustomResult<String> {
        try {
            if(result.resultCode==Activity.RESULT_OK){
                val intent = result.data ?: return CustomResult.Error(CallErrors.ErrorEmptyResponse)
                val status = intent.getStringExtra("status")
                val message = intent.getStringExtra("message")
                if (status == null || status != "success") {
                    return CustomResult.Error(CallErrors.ErrorWithMessage(message.toString()))
                }
                val signatureBytes = intent.getByteArrayExtra("signature")
                val certificateBytes = intent.getByteArrayExtra("certificate")
                val cf = CertificateFactory.getInstance("X.509")
                val certStream: InputStream = ByteArrayInputStream(certificateBytes)
                val certificate = cf.generateCertificate(certStream) as X509Certificate
                val s = Signature.getInstance(signAlgorithm)
                s.initVerify(certificate)
                s.update(ByteArray(64))
                if (s.verify(signatureBytes)) {
                    val subject = certificate.subjectDN
                    return CustomResult.Success(subject.toString())
                } else {
                    return CustomResult.Error(CallErrors.ErrorVerificationError)
                }
            }else if (result.resultCode == Activity.RESULT_CANCELED){
                return CustomResult.Error(CallErrors.ErrorOperationCancelled)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return CustomResult.Error(CallErrors.ErrorParsing)
        }
        return CustomResult.Error(CallErrors.ErrorWithMessage("unknown"))
    }
}