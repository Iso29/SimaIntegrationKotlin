package com.example.simaintegrationkotlin.ui.pickAndSignFragment.fragmentItems

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import com.example.domain.launcher.Launcher
import com.example.simaintegrationkotlin.common.base.BaseFragment
import com.example.simaintegrationkotlin.common.extention.args
import com.example.simaintegrationkotlin.databinding.FragmentSignChallangeBinding
import com.example.simaintegrationkotlin.ui.pickAndSignFragment.PickAndSignViewModel

class SignChallengeFragment : BaseFragment<FragmentSignChallangeBinding>() {
    private var pickPDFLauncher : Launcher.SignChallengeResultLauncher<ActivityResultLauncher<Intent>>?=null
    private val viewModel : PickAndSignViewModel? by args(VIEW_MODEL)
    override val onInflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSignChallangeBinding
        get() = FragmentSignChallangeBinding::inflate
    override val onBind: FragmentSignChallangeBinding.() -> Unit
        get() = {

        }

    companion object{
        fun newInstance(): SignChallengeFragment {
            val args = Bundle()
            val fragment = SignChallengeFragment()
            fragment.arguments = args
            return fragment
        }
    }
}