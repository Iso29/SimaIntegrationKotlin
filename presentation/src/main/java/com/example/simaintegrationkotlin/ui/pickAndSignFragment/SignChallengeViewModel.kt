package com.example.simaintegrationkotlin.ui.pickAndSignFragment

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.lifecycle.SavedStateHandle
import com.example.data.common.CustomResult
import com.example.data.managersImpl.SignChallengeImpl
import com.example.data.utils.CallErrors
import com.example.domain.requestModel.SimaParamsSignChallengeRequest
import com.example.simaintegrationkotlin.common.base.BaseViewModel
import java.io.Serializable

class SignChallengeViewModel(savedStateHandle: SavedStateHandle?=null, val signChallengeImpl: SignChallengeImpl)
    : BaseViewModel<SignChallengeViewModel.State>(savedStateHandle), Serializable
{
    data class State(
        var intentForSignChallenge : CustomResult<Intent>?=null,
        var resultIntentForSignChallenge : CustomResult<String>?=null
    )

    override val initialState: State?
        get() = State()

    fun startSignChallenge(context : Context,simaParamsSignChallengeRequest: SimaParamsSignChallengeRequest){
        stateValue?.let {
            postState(it.copy(intentForSignChallenge = CustomResult.Loading))
            val result =  signChallengeImpl.startSignChallenge(context,simaParamsSignChallengeRequest)
            if(result!=null){
                postState(it.copy(intentForSignChallenge = CustomResult.Success(result)))
            }else{
                postState(it.copy(intentForSignChallenge = CustomResult.Error(CallErrors.ErrorIntent)))
            }
        }
    }

    fun handleSignIntent(result : ActivityResult, signAlgorithm : String){
        stateValue?.let {
            postState(it.copy(resultIntentForSignChallenge = CustomResult.Loading))
            val result = signChallengeImpl.handleSignIntent(result ,signAlgorithm)
            postState(it.copy(resultIntentForSignChallenge = result))
        }
    }

    fun resetIntent(){
        stateValue?.let {
            postState(it.copy(intentForSignChallenge = null))
        }
    }

    fun resetResult(){
        stateValue?.let {
            postState(it.copy(resultIntentForSignChallenge = null))
        }
    }
}