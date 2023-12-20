package com.example.simaintegrationkotlin.ui.pickAndSignFragment.fragmentItems

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.simaintegrationkotlin.common.base.BaseFragment
import com.example.simaintegrationkotlin.databinding.FragmentSignChallangeBinding
import com.example.simaintegrationkotlin.ui.pickAndSignFragment.PickAndSignViewModel

class SignChallengeFragment : BaseFragment<FragmentSignChallangeBinding>() {
    override val onInflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSignChallangeBinding
        get() = FragmentSignChallangeBinding::inflate
    override val onBind: FragmentSignChallangeBinding.() -> Unit
        get() = {

        }

    companion object{
        private const val VIEW_MODEL = "view_model"

        fun newInstance(
            viewModel: PickAndSignViewModel?
        ): SignChallengeFragment {
            val args = Bundle()
            args.putSerializable(VIEW_MODEL, viewModel)
            val fragment = SignChallengeFragment()
            fragment.arguments = args
            return fragment
        }
    }
}