package com.example.simaintegrationkotlin.ui.pickAndSignFragment.fragmentItems

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.simaintegrationkotlin.common.base.BaseFragment
import com.example.simaintegrationkotlin.databinding.FragmentSignPdfBinding
import com.example.simaintegrationkotlin.ui.pickAndSignFragment.PickAndSignViewModel
import dagger.hilt.android.AndroidEntryPoint

class SignPdfFragment : BaseFragment<FragmentSignPdfBinding>(){
    override val onInflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSignPdfBinding
        get() = FragmentSignPdfBinding::inflate
    override val onBind: FragmentSignPdfBinding.() -> Unit
        get() = {

        }

    companion object{
        private const val VIEW_MODEL = "view_model"

        fun newInstance(
            viewModel: PickAndSignViewModel?,
        ): SignPdfFragment {
            val args = Bundle()
            args.putSerializable(VIEW_MODEL, viewModel)
            val fragment = SignPdfFragment()
            fragment.arguments = args
            return fragment
        }
    }
}