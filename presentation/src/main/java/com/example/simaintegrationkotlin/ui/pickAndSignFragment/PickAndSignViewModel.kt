package com.example.simaintegrationkotlin.ui.pickAndSignFragment

import androidx.lifecycle.SavedStateHandle
import com.example.simaintegrationkotlin.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.Serializable
import javax.inject.Inject

@HiltViewModel
class PickAndSignViewModel @Inject constructor(savedStateHandle: SavedStateHandle)
    : BaseViewModel<PickAndSignViewModel.State>(savedStateHandle),Serializable
{
        data class State(
            var response : String=""
        )

    override val initialState: State?
        get() = State()
}