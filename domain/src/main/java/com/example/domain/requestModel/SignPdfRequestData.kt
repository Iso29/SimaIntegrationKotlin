package com.example.domain.requestModel

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.domain.keys.OperationTypes

data class SignPdfRequestData(
    val documentUri : Uri,
        var intent : Intent,
    val context : Context,
    val fin : String,
    val operationTypes: OperationTypes,
    val clientHashAlgorithm : String,
    val clientSignatureAlgorithm : String,
    val clientMasterKey : String,
    val extraServiceValue : String,
    val extraClientIdValue : Int,
    val logo : String,
    val uuid: String
    ) {
}