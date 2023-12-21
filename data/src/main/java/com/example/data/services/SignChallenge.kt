package com.example.data.services

import android.content.Context
import android.content.Intent
import com.example.domain.requestModel.SimaParamsSignChallengeRequest

interface SignChallenge {
    fun startSignChallenge(context : Context, simaParams : SimaParamsSignChallengeRequest):Intent?
}