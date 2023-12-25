package com.example.simaintegrationkotlin.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.data.managersImpl.PickPdfServiceImpl
import com.example.data.managersImpl.SignChallengeImpl
import com.example.data.managersImpl.SignPdfServiceImpl
import com.example.simaintegrationkotlin.ui.pickAndSignFragment.PickAndSignViewModel
import com.example.simaintegrationkotlin.ui.pickAndSignFragment.SignChallengeViewModel


class SignChallengeViewModelFactory(private val signChallengeImpl: SignChallengeImpl) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignChallengeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SignChallengeViewModel(signChallengeImpl = signChallengeImpl) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class PickAndSignViewModelFactory(private val pickPdfServiceImpl: PickPdfServiceImpl,private val signPdfServiceImpl: SignPdfServiceImpl):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PickAndSignViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PickAndSignViewModel(pickPdfServiceImpl = pickPdfServiceImpl, signPdfServiceImpl = signPdfServiceImpl) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
