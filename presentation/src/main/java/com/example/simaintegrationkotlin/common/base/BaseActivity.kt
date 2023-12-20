package com.example.simaintegrationkotlin.common.base

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import java.util.Locale

abstract class BaseActivity<Binding : ViewBinding> : AppCompatActivity() {

    private var _binding: Binding? = null
    val binding: Binding
        get() = _binding!!

    abstract val onInflate: (LayoutInflater) -> Binding
    abstract val onBind: Binding.() -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = onInflate(layoutInflater)
        setContentView(binding.root)
        binding.onBind()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        val config = Configuration()
        applyOverrideConfiguration(config)
    }

    override fun applyOverrideConfiguration(newConfig: Configuration) {
        super.applyOverrideConfiguration(updateConfigurationIfSupported(newConfig))
    }

    @Suppress("DEPRECATION")
    open fun updateConfigurationIfSupported(config: Configuration,languageCode : String ="en"): Configuration? {
        if (!config.locales.isEmpty) {
            return config
        }
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        config.setLocale(locale)
        return config
    }





}