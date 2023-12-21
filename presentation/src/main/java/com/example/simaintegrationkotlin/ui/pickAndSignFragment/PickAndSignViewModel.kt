package com.example.simaintegrationkotlin.ui.pickAndSignFragment

import android.content.Intent
import androidx.lifecycle.SavedStateHandle
import com.example.data.common.CustomResult
import com.example.data.managersImpl.PickPdfServiceImpl
import com.example.data.utils.CallErrors
import com.example.domain.responseModel.PickPdfResponseData
import com.example.simaintegrationkotlin.common.base.BaseViewModel
import java.io.Serializable

//@HiltViewModel
class PickAndSignViewModel constructor(savedStateHandle: SavedStateHandle,val pickPdfServiceImpl: PickPdfServiceImpl)
    : BaseViewModel<PickAndSignViewModel.State>(savedStateHandle),Serializable
{
        data class State(
            var pickIntent : CustomResult<Intent>?=null,
            var actionIntentAfterPick : CustomResult<PickPdfResponseData>?=null
        )

    override val initialState: State?
        get() = State()

    fun startPickIntent(){
        stateValue?.let {
            postState(it.copy(pickIntent = CustomResult.Loading))
            val result =  pickPdfServiceImpl.startPickIntent()
            if(result!=null){
                postState(it.copy(pickIntent = CustomResult.Success(result)))
            }else{
                postState(it.copy(pickIntent = CustomResult.Error(CallErrors.ErrorIntent)))
            }
        }
    }
}