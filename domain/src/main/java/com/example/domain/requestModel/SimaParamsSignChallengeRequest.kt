package com.example.domain.requestModel

import com.example.domain.keys.OperationTypes


data class SimaParamsSignChallengeRequest(
    val packageName : String,
    val clientHashAlgorithm : String,
    val clientSignatureAlgorithm : String,
    val clientMasterKey : String,
    val operationType : OperationTypes,
    val extraServiceValue : String,
    val extraClientIdValue : Int,
    val logo : String,
    val uuid : String
) {
}