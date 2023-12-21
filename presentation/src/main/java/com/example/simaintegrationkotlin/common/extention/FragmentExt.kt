package com.example.simaintegrationkotlin.common.extention

import android.os.Build
import android.widget.Toast
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

fun Fragment.makeToast(msg : String){
    Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
}