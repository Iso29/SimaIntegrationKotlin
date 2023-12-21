package com.example.data.common

import com.example.data.utils.CallErrors

sealed class CustomResult<out T> {
    data class Success<out T>(val data : T):CustomResult<T>()
    data class Error(val exception: CallErrors) : CustomResult<Nothing>()
    object Loading : CustomResult<Nothing>()
}