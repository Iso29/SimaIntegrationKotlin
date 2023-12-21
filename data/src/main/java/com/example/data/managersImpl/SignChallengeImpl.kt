package com.example.data.managersImpl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.data.keys.FieldKeys
import com.example.data.services.SignChallenge
import com.example.domain.requestModel.SimaParamsSignChallengeRequest
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class SignChallengeImpl : SignChallenge {
    override fun startSignChallenge(
        context: Context,
        simaParams: SimaParamsSignChallengeRequest
    ): Intent? {
        var intent: Intent? =
            context.packageManager.getLaunchIntentForPackage(FieldKeys.PACKAGE_NAME)

        if (intent == null) {
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
            val random = SecureRandom()
            val challenge = ByteArray(64)
            random.nextBytes(challenge)
            val md = MessageDigest.getInstance(simaParams.hashAlgorithm)
            md.update(challenge)
            val hash = md.digest()
            val mac = Mac.getInstance(simaParams.signatureAlgorithm)
            mac.init(
                SecretKeySpec(
                    simaParams.masterkey.toByteArray(),
                    simaParams.signatureAlgorithm
                )
            )
            val signature = mac.doFinal(hash)
            intent = intent.setAction(simaParams.operationType.op)
                .setFlags(0)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                .putExtra(FieldKeys.EXTRA_CHALLENGE_FIELD, challenge)
                .putExtra(FieldKeys.EXTRA_SERVICE_FIELD, simaParams.serviceValue)
                .putExtra(FieldKeys.EXTRA_CLIENT_ID_FIELD, simaParams.clientId)
                .putExtra(FieldKeys.EXTRA_SIGNATURE_FIELD, signature)
                .putExtra(FieldKeys.EXTRA_LOGO_FIELD, simaParams.logo)
                .putExtra(FieldKeys.EXTRA_REQUEST_ID_FIELD, simaParams.uuid)
        }
        return intent
    }

}