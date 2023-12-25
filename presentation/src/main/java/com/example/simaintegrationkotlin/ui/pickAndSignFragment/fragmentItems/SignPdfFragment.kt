package com.example.simaintegrationkotlin.ui.pickAndSignFragment.fragmentItems

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.example.data.common.CustomResult
import com.example.data.managersImpl.PickPdfServiceImpl
import com.example.data.managersImpl.SignPdfServiceImpl
import com.example.data.utils.PermissionUtils
import com.example.data.utils.extractMessage
import com.example.domain.keys.OperationTypes
import com.example.domain.keys.UserKeys
import com.example.domain.launcher.Launcher
import com.example.domain.requestModel.SignPdfRequestData
import com.example.simaintegrationkotlin.common.base.BaseFragment
import com.example.simaintegrationkotlin.common.extention.getLogo
import com.example.simaintegrationkotlin.common.extention.makeToast
import com.example.simaintegrationkotlin.databinding.FragmentSignPdfBinding
import com.example.simaintegrationkotlin.ui.common.PickAndSignViewModelFactory
import com.example.simaintegrationkotlin.ui.pickAndSignFragment.PickAndSignViewModel
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import java.util.UUID

class SignPdfFragment : BaseFragment<FragmentSignPdfBinding>(),PermissionUtils.PermissionResultListener{
    private var pickPDFLauncher : Launcher.PickPdfResultLauncher<ActivityResultLauncher<Intent>>?=null
    private var signPdfLauncher : Launcher.SignPdfResultLauncher<ActivityResultLauncher<Intent>>?=null
    private var createDocLauncer : Launcher.CreatePDFResultLauncher<ActivityResultLauncher<Intent>>?=null
    private var viewModel : PickAndSignViewModel?=null

    private var fileToSave : Uri?=null
    private var documentUri : Uri?=null
    override val onInflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSignPdfBinding
        get() = FragmentSignPdfBinding::inflate
    override val onBind: FragmentSignPdfBinding.() -> Unit
        get() = {
            PDFBoxResourceLoader.init(requireContext())
            initViewModel()
            initLaunchers()
            viewModel?.state?.observe(this@SignPdfFragment,::observeState)
        }

    private fun initViewModel(){
        viewModel = ViewModelProvider(
            this,
            PickAndSignViewModelFactory(pickPdfServiceImpl = PickPdfServiceImpl(), signPdfServiceImpl = SignPdfServiceImpl())
        )[PickAndSignViewModel::class.java]
    }

    private fun initLaunchers(){
        pickPDFLauncher = Launcher.PickPdfResultLauncher(launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->

        })

        signPdfLauncher = Launcher.SignPdfResultLauncher(launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult? ->
            result?.let { viewModel?.verifySign(requireContext(),it) }
        })

        createDocLauncer = Launcher.CreatePDFResultLauncher(launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult? ->
            if(fileToSave!=null && result!=null){
                viewModel?.pickDirectoryResult(result,requireContext(),fileToSave!!)
            }
        })
    }

    private fun observeState(state : PickAndSignViewModel.State){
        state.let { st->
            st.createdPdf?.let { response->
                when(response){
                    is CustomResult.Success->{
                        if(response.data.isEligibleToSign){
                            val fin = binding?.finEditText?.text.toString().trim()
                            if(fin.isNotEmpty()){
                                response.data.file?.let {
                                    documentUri = FileProvider.getUriForFile(
                                        requireContext(), "${requireContext().packageName}.fileprovider",it
                                    )
                                }
                                makeToast("success")
                                viewModel?.signPdf(
                                    SignPdfRequestData(documentUri!!,
                                    response.data.intent,
                                    requireContext(),
                                    fin,
                                    OperationTypes.SIGN_PDF_OPERATION,
                                    UserKeys.CLIENT_HASH_ALGORITHM,
                                    UserKeys.CLIENT_SIGNATURE_ALGORITHM,
                                    UserKeys.CLIENT_MASTER_KEY,
                                    UserKeys.EXTRA_SERVICE_VALUE,
                                    UserKeys.EXTRA_CLIENT_ID_VALUE,
                                    getLogo("logo.png")?:"",
                                    UUID.randomUUID().toString())
                                )
                            }else{
                                makeToast("enter fin")
                            }
                        }else{
                            makeToast("success intent")
                            startActivity(response.data.intent)
                        }
                    }
                    is CustomResult.Error->response.exception.extractMessage()
                    is CustomResult.Loading->{makeToast("loading...")}
                }
            }

            st.signPdfIntent?.let { response->
                when(response){
                    is CustomResult.Success->{signPdfLauncher?.launcher?.launch(response.data)}
                    is CustomResult.Error-> response.exception.extractMessage()
                    is CustomResult.Loading->makeToast("loading...")
                }
            }

            st.verifyResult?.let { response->
                when(response){
                    is CustomResult.Success->{
                        fileToSave = response.data.documentUri
                        createDocLauncer?.launcher?.launch(response.data.intent)
                    }
                    is CustomResult.Error->response.exception.extractMessage()
                    is CustomResult.Loading->{makeToast("loading...")}
                }
            }

            st.createDir?.let { response->
                when(response){
                    is CustomResult.Success->makeToast(response.data)
                    is CustomResult.Error->response.exception.extractMessage()
                    is CustomResult.Loading->makeToast("loading")
                }
            }
        }
    }

    override fun FragmentSignPdfBinding.setListeners(){
        createAndSignPDFButton.setOnClickListener { viewModel?.createSignPdf(requireContext()) }
    }
    companion object{
        fun newInstance(): SignPdfFragment {
            val args = Bundle()
            val fragment = SignPdfFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                viewModel?.startPickIntent()
            } else {
                makeToast( "Storage permission denied")
            }
        }
    }
    override fun onPermissionResult(
        isItAllowed: Boolean,
        isShouldShowRequestPermission: Boolean,
        permission: String
    ) {
        viewModel?.startPickIntent()
    }
}