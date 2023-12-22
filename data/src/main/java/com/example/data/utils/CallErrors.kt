package com.example.data.utils

import org.bouncycastle.cms.SignerInformation

sealed class CallErrors {
    object ErrorIntent: CallErrors()
    object ErrorEmptyResponse: CallErrors()
    object ErrorNoSigner : CallErrors()
    object ErrorNoCertifate : CallErrors()

    data class ErrorWithMessage(val msg : String) : CallErrors()
    data class ErrorDoesNotMatchCertificates(val signerInformation: SignerInformation) : CallErrors()
    object ErrorVerificationError : CallErrors()
    object ErrorCreatePDF : CallErrors()
    object ErrorPermissionDenied : CallErrors()
    object ErrorWrongOperation : CallErrors()
    object ErrorInternal : CallErrors()
    object ErrorSignChallenge : CallErrors()
    object ErrorSignDocumentError : CallErrors()
    object ErrorApproveRequest : CallErrors()
    object ErrorTimeStamp : CallErrors()
    object ErrorValidateRequest : CallErrors()
    object ErrorProcessingChallenge : CallErrors()
    object ErrorProcessingDocument : CallErrors()
    object ErrorLogoSize : CallErrors()
    object ErrorLogoFormat : CallErrors()
    object ErrorUserCode : CallErrors()
    object ErrorEmptySign : CallErrors()
    object ErrorEmptyData : CallErrors()
    object ErrorEmptyService : CallErrors()
    object ErrorEmptyClientId : CallErrors()
    object ErrorOperationCancelled : CallErrors()
    object ErrorParsing : CallErrors()
    object ErrorNoDirectoryPicked : CallErrors()
    object ErrorSavingFile : CallErrors()
}