package com.example.simaintegrationkotlin.common.extention

import android.os.Build
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

fun Fragment.popBackStack() = findNavController().popBackStack()

@Suppress("DEPRECATION")
inline fun <reified T : java.io.Serializable> Fragment.args(key: String) = lazy {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arguments?.getSerializable(key, T::class.java)
    } else {
        arguments?.getSerializable(key) as? T
    }
}