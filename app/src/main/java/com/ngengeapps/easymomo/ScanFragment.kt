package com.ngengeapps.easymomo

import android.Manifest.permission.CAMERA
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.ngengeapps.easymomo.databinding.FragmentScanBinding
import com.ngengeapps.easymomo.models.Account
import com.ngengeapps.easymomo.models.TrimmedAccount

private val MY_CAMERA_REQUEST_CODE = 100


class ScanFragment : Fragment() {


    private lateinit var binding: FragmentScanBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


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

        setScannerProperties()
        val permission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted: Boolean ->
                if (granted) {
                    binding.scannerView.setResultHandler {
                        if (it.text != null) {

                            try {
                                val gson = Gson()
                                val account = gson.fromJson(it.text, TrimmedAccount::class.java)
                                val action =
                                    ScanFragmentDirections.actionNavigationScanToNavigationTransfer(
                                        account
                                    )
                                findNavController().navigate(action)

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
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentScanBinding.inflate(inflater)
        return binding.root
    }


}