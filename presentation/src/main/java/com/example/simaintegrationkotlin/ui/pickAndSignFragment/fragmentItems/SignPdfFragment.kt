package com.example.simaintegrationkotlin.ui.pickAndSignFragment.fragmentItems

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.data.common.CustomResult
import com.example.domain.launcher.Launcher
import com.example.simaintegrationkotlin.common.base.BaseFragment
import com.example.simaintegrationkotlin.common.extention.makeToast
import com.example.simaintegrationkotlin.databinding.FragmentSignPdfBinding
import com.example.simaintegrationkotlin.ui.pickAndSignFragment.PickAndSignViewModel

class SignPdfFragment : BaseFragment<FragmentSignPdfBinding>(){
    private var pickPDFLauncher : Launcher.PickPdfResultLauncher<ActivityResultLauncher<Intent>>?=null
    override val onInflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSignPdfBinding
        get() = FragmentSignPdfBinding::inflate
    override val onBind: FragmentSignPdfBinding.() -> Unit
        get() = {
            initLaunchers()
//            viewModel?.state?.observe(this@SignPdfFragment,::observeState)
        }


    private fun initLaunchers(){
        pickPDFLauncher = Launcher.PickPdfResultLauncher(launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val documentUri = result.data!!.data
            }
        })
    }

    private fun observeState(state : PickAndSignViewModel.State){
        state.let { st->
            st.pickIntent?.let { pickIntent->
                when(pickIntent){
                    is CustomResult.Loading-> makeToast("loading")
                    is CustomResult.Success-> pickPDFLauncher?.launcher?.launch(pickIntent.data)
                    is CustomResult.Error -> makeToast("error")
                }
            }
        }
    }

    override fun FragmentSignPdfBinding.setListeners(){
//        pickAndSignPDFButton.setOnClickListener { viewModel?.startPickIntent() }
    }
    companion object{

        fun newInstance(): SignPdfFragment {
            val args = Bundle()
            val fragment = SignPdfFragment()
            fragment.arguments = args
            return fragment
        }
    }
}