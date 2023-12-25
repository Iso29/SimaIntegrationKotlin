package com.example.simaintegrationkotlin.ui.pickAndSignFragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.lifecycle.SavedStateHandle
import com.example.data.common.CustomResult
import com.example.data.managersImpl.PickPdfServiceImpl
import com.example.data.managersImpl.SignPdfServiceImpl
import com.example.domain.requestModel.SignPdfRequestData
import com.example.domain.responseModel.PickPdfResponseData
import com.example.domain.responseModel.VerifySignResponseData
import com.example.simaintegrationkotlin.common.base.BaseViewModel
import java.io.Serializable

//@HiltViewModel
class PickAndSignViewModel(savedStateHandle: SavedStateHandle?=null,val pickPdfServiceImpl: PickPdfServiceImpl,val signPdfServiceImpl: SignPdfServiceImpl)
    : BaseViewModel<PickAndSignViewModel.State>(savedStateHandle),Serializable
{
        data class State(
           var createdPdf : CustomResult<PickPdfResponseData>?=null,
            var signPdfIntent : CustomResult<Intent>?=null,
            var pickIntent : CustomResult<Intent>?=null,
            var verifyResult : CustomResult<VerifySignResponseData>?=null,
            var createDir : CustomResult<String>?=null
        )

    override val initialState: State?
        get() = State()


    fun createSignPdf(context: Context){
        stateValue?.let {
            setState(it.copy(createdPdf = pickPdfServiceImpl.createSignPdf(context)))
        }
    }

    fun signPdf(signPdfRequestData: SignPdfRequestData){
        stateValue?.let {
            setState(it.copy(createdPdf = null,signPdfIntent = CustomResult.Loading))
            when(val response = signPdfServiceImpl.signPdf(signPdfRequestData)){
                is CustomResult.Success->setState(it.copy(signPdfIntent = response))
                is CustomResult.Error->setState(it.copy(signPdfIntent = response))
                is CustomResult.Loading-> println()
            }
        }
    }
    fun startPickIntent(){
        stateValue?.let {
            setState(it.copy(pickIntent = CustomResult.Loading))
            when(val response = pickPdfServiceImpl.startPickIntent()){
                is CustomResult.Success->setState(it.copy(pickIntent = response))
                is CustomResult.Error->setState(it.copy(pickIntent = response))
                is CustomResult.Loading->println()
            }
        }
    }

    fun verifySign(context: Context,result: ActivityResult){
        stateValue?.let {
            setState(it.copy(signPdfIntent = null, verifyResult = CustomResult.Loading))
            when(val response = signPdfServiceImpl.verifySign(context,result)){
                is CustomResult.Success-> setState(it.copy(verifyResult = response))
                is CustomResult.Error-> setState(it.copy(verifyResult = response))
                is CustomResult.Loading->println()
            }
        }
    }

    fun pickDirectoryResult(result: ActivityResult,context: Context,fileToSave : Uri){
        stateValue?.let {
            setState(it.copy(verifyResult = null,createDir = CustomResult.Loading))
            when(val response = pickPdfServiceImpl.pickDirectoryResult(result, context, fileToSave)){
                is CustomResult.Success->{
                    setState(it.copy(createDir = response))
                }
                is CustomResult.Error->{
                    setState(it.copy(createDir = response))
                }
                is CustomResult.Loading-> println()
            }
        }
    }

}