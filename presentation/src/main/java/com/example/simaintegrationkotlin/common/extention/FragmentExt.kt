package com.example.simaintegrationkotlin.common.extention

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import java.io.ByteArrayOutputStream
import java.io.IOException
import kotlin.reflect.KClass

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

fun <T : ViewModel> Fragment.viewModelProvider(
    factory: ViewModelProvider.Factory,
    model: KClass<T>
): T {
    return ViewModelProvider(this, factory)[model.java]
}

fun Fragment.getLogo(logoName : String) : String?{
    try {
        val assetManager: AssetManager = requireActivity().assets
        val logoFile = assetManager.open(logoName)
        val bitmap = BitmapFactory.decodeStream(logoFile)
        logoFile.close()

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()

        return Base64.encodeToString(byteArray, Base64.NO_PADDING)
    }catch (e:IOException){
        e.printStackTrace()
    }
    return null
}

fun Fragment.errorLog(message:String){
    Log.e("DEMO",message)
}