package com.ngengeapps.easymomo.ui

import android.Manifest.permission.CAMERA
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.ngengeapps.easymomo.R
import com.ngengeapps.easymomo.databinding.FragmentScanBinding
import com.ngengeapps.easymomo.models.TrimmedAccount
import com.ngengeapps.easymomo.viewmodels.AccountListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


const val TAG = "SCANNER-"
@AndroidEntryPoint
class ScanFragment : Fragment() {

    private lateinit var binding: FragmentScanBinding
    val viewmodel:AccountListViewModel by activityViewModels()
    @Inject
    lateinit var gson: Gson

    private fun setScannerProperties() {
        binding.scannerView.apply {
            setFormats(listOf(BarcodeFormat.QR_CODE))
            setAutoFocus(true)
            setLaserColor(R.color.colorSecondary)
            setMaskColor(R.color.colorSecondary)
            if (Build.MANUFACTURER.equals("HUAWEI", ignoreCase = true)) {
                setAspectTolerance(0.5f)
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navController = view.findNavController()

        setScannerProperties()
        val permission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted: Boolean ->
                if (granted) {
                    binding.scannerView.setResultHandler {
                        if (it.text != null) {

                            try {
                                val account = gson.fromJson(it.text, TrimmedAccount::class.java)
                                viewmodel.setAccount(account)
                                val action =
                                    ScanFragmentDirections.actionNavigationScanToNavigationTransfer(
                                    )
                                navController.navigate(action)

                            } catch (ex: Exception) {

                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.invalid_scanned_contact),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }


                        }
                    }

                    binding.scannerView.startCamera()

                } else {

                    Toast.makeText(requireContext(), getString(R.string.permission_momo_required), Toast.LENGTH_SHORT).show()
                }
            }

        permission.launch(CAMERA)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScanBinding.inflate(inflater)
        return binding.root
    }


    override fun onPause() {
        super.onPause()
        binding.scannerView.apply {
            Log.d(TAG, "onPause: Stopping Camera")
            stopCamera()
        }
    }




}