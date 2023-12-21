package com.example.simaintegrationkotlin.ui.pickAndSignFragment

import android.content.Context
import android.content.Intent
import androidx.lifecycle.SavedStateHandle
import com.example.data.common.CustomResult
import com.example.data.managersImpl.SignChallengeImpl
import com.example.data.utils.CallErrors
import com.example.domain.requestModel.SimaParamsSignChallengeRequest
import com.example.domain.responseModel.PickPdfResponseData
import com.example.simaintegrationkotlin.common.base.BaseViewModel
import java.io.Serializable

class SignChallengeViewModel constructor(savedStateHandle: SavedStateHandle, val signChallengeImpl: SignChallengeImpl)
    : BaseViewModel<SignChallengeViewModel.State>(savedStateHandle), Serializable
{
    data class State(
        var pickIntent : CustomResult<Intent>?=null,
        var actionIntentAfterPick : CustomResult<PickPdfResponseData>?=null
    )

    override val initialState: State?
        get() = State()

    fun startSignChallenge(context : Context,simaParamsSignChallengeRequest: SimaParamsSignChallengeRequest){
        stateValue?.let {
            postState(it.copy(pickIntent = CustomResult.Loading))
            val result =  signChallengeImpl.startSignChallenge(context,simaParamsSignChallengeRequest)
            if(result!=null){
                postState(it.copy(pickIntent = CustomResult.Success(result)))
            }else{
                postState(it.copy(pickIntent = CustomResult.Error(CallErrors.ErrorIntent)))
            }
        }
    }

    fun handleSignIntent(intent: Intent?){}
}