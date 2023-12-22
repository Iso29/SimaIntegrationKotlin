package com.example.simaintegrationkotlin.ui.pickAndSignFragment.fragmentItems

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.data.common.CustomResult
import com.example.data.managersImpl.SignChallengeImpl
import com.example.data.utils.extractMessage
import com.example.domain.keys.FieldKeys
import com.example.domain.keys.OperationTypes
import com.example.domain.keys.UserKeys
import com.example.domain.launcher.Launcher
import com.example.domain.requestModel.SimaParamsSignChallengeRequest
import com.example.simaintegrationkotlin.common.base.BaseFragment
import com.example.simaintegrationkotlin.common.extention.getLogo
import com.example.simaintegrationkotlin.common.extention.makeToast
import com.example.simaintegrationkotlin.databinding.FragmentSignChallangeBinding
import com.example.simaintegrationkotlin.ui.common.SignChallengeViewModelFactory
import com.example.simaintegrationkotlin.ui.pickAndSignFragment.SignChallengeViewModel
import java.util.UUID

class SignChallengeFragment : BaseFragment<FragmentSignChallangeBinding>() {
    private var signChallengeLauncher : Launcher.SignChallengeResultLauncher<ActivityResultLauncher<Intent>>?=null
    private var viewModel : SignChallengeViewModel?=null

    override val onInflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSignChallangeBinding
        get() = FragmentSignChallangeBinding::inflate
    override val onBind: FragmentSignChallangeBinding.() -> Unit
        get() = {
            initViewModel()
            initLaunchers()
            viewModel?.state?.observe(this@SignChallengeFragment,::observeData)
        }

  private fun initViewModel(){
      viewModel = ViewModelProvider(this, SignChallengeViewModelFactory(SignChallengeImpl()))[SignChallengeViewModel::class.java]
  }

    private fun initLaunchers(){
        signChallengeLauncher = Launcher.SignChallengeResultLauncher(launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            viewModel?.handleSignIntent(result,UserKeys.SIMA_SIGNATURE_ALGORITHM)
        })
    }

    private fun observeData(state : SignChallengeViewModel.State){
        state.let {
            it.intentForSignChallenge?.let { result->
                when(result){
                    is CustomResult.Success-> {
                        signChallengeLauncher?.launcher?.launch(result.data)
                        viewModel?.resetIntent()
                    }
                    is CustomResult.Error -> {
                        makeToast(result.exception.extractMessage())
                    }
                    is CustomResult.Loading->{
                    }
                }
            }

            it.resultIntentForSignChallenge?.let { result->
                when(result){
                    is CustomResult.Success -> {
                        makeToast(result.data)
                    }
                    is CustomResult.Error -> {
                        makeToast(result.exception.extractMessage())
                        viewModel?.resetResult()
                    }
                    is CustomResult.Loading->{
                    }
                }
            }
        }
    }

    override fun FragmentSignChallangeBinding.setListeners(){
        signChallengeButton.setOnClickListener { viewModel?.startSignChallenge(
            requireActivity(),
            SimaParamsSignChallengeRequest(
                FieldKeys.PACKAGE_NAME,
                UserKeys.CLIENT_HASH_ALGORITHM,
                UserKeys.CLIENT_SIGNATURE_ALGORITHM,
                UserKeys.CLIENT_MASTER_KEY,
                OperationTypes.SIGN_CHALLENGE_OPERATION,
                UserKeys.EXTRA_SERVICE_VALUE,
                UserKeys.EXTRA_CLIENT_ID_VALUE,
                getLogo("logo.png")?:"",
                UUID.randomUUID().toString())
        ) }
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