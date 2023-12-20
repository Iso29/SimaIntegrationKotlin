package com.example.simaintegrationkotlin.common.base

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<Binding : ViewBinding> : Fragment() {

    private var _binding: Binding? = null
    protected val binding: Binding?
        get() = _binding!!

    abstract val onInflate: (LayoutInflater, ViewGroup?, Boolean) -> Binding
    abstract val onBind: Binding.() -> Unit

    val screenBackground: Drawable
        get() = binding?.root!!.background

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = onInflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.onBind()
        binding?.setListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    open fun Binding.setListeners() {}

}