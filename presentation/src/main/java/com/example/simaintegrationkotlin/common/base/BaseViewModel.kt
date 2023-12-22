package com.example.simaintegrationkotlin.common.base

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavArgs

abstract class BaseViewModel<State>(
    val savedStateHandle: SavedStateHandle?
) : ViewModel() {

    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state

    val stateValue: State?
        get() = state.value

    abstract val initialState: State?

    init {
        _state.value = initialState
    }

    fun postState(state: State?) {
        state?.let { _state.postValue(it) }
    }

    @MainThread
    fun setState(state: State?) {
        state?.let { _state.value = it }
    }

    fun doOnUIThread(block: () -> Unit) = Handler(Looper.getMainLooper()).post(block)

    inline fun <reified Args : NavArgs> getArgs(): Args = Args::class.java.run {
        val argsBundle = Bundle().apply {
            savedStateHandle?.keys()?.forEach {
                putSerializable(it, savedStateHandle[it])
            }
        }
        val method = getDeclaredMethod("fromBundle", Bundle::class.java)
        val args = method.invoke(null, argsBundle) as Args
        args
    }

}