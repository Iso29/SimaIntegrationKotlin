package com.example.data.services

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import com.example.data.common.CustomResult
import com.example.domain.requestModel.SimaParamsSignChallengeRequest

interface SignChallenge {
    fun startSignChallenge(context : Context, simaParams : SimaParamsSignChallengeRequest):Intent?

    fun handleSignIntent(result: ActivityResult,signAlgorithm : String) : CustomResult<String>
}