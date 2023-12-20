package com.example.data.common

import com.example.data.utils.CallErrors
import java.lang.Exception

sealed class CustomResult<out T> {
    data class Success<out T>(val data : T):CustomResult<T>()
    data class Error(val exception: CallErrors) : CustomResult<CallErrors>()
    object Loading : CustomResult<Nothing>()
}