package com.example.simaintegrationkotlin.ui.activities

import android.view.LayoutInflater
import com.example.simaintegrationkotlin.common.base.BaseActivity
import com.example.simaintegrationkotlin.databinding.ActivityMainBinding

//@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val onInflate: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate
    override val onBind: ActivityMainBinding.() -> Unit
        get() = {

        }


}