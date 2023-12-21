package com.example.domain.requestModel

import com.example.data.keys.OperationTypes

data class SimaParamsSignChallengeRequest(
    val packageName : String,
    val hashAlgorithm : String,
    val signatureAlgorithm : String,
    val masterkey : String,
    val operationType : OperationTypes,
    val serviceValue : String,
    val clientId : String,
    val logo : String,
    val uuid : String
) {
}