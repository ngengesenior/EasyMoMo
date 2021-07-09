package com.ngengeapps.easymomo.ui

import android.Manifest.permission.CALL_PHONE
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ngengeapps.easymomo.R
import com.ngengeapps.easymomo.databinding.FragmentTransferBinding
import com.ngengeapps.easymomo.models.PhoneType
import com.ngengeapps.easymomo.utils.dialUSSD
import com.ngengeapps.easymomo.utils.getNumberType
import com.ngengeapps.easymomo.utils.openSettings
import com.ngengeapps.easymomo.viewmodels.AccountListViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TransferFragment : Fragment() {

    private lateinit var binding: FragmentTransferBinding
    lateinit var permission:ActivityResultLauncher<String>
    val viewModel:AccountListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTransferBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        //binding.account = viewModel.selectedAccount

        binding.buttonSend.setOnClickListener {
            val amount = binding.editTextAmount.text.toString().trim()
            if (amount.isDigitsOnly()) {

                if (ContextCompat.checkSelfPermission(requireContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                    val number = binding.textViewPhoneNumber.text.toString().trim()

                    Log.d(TAG, "onCreateView: The number is $number")
                    requireContext().dialUSSD(number,amount)

                    val action = TransferFragmentDirections.actionNavigationTransferToNavigationMyQr()
                    findNavController().navigate(action)


                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), CALL_PHONE)) {
                        Toast.makeText(requireContext(),getString(R.string.perm_phone),Toast.LENGTH_LONG).show()
                        permission.launch(CALL_PHONE)
                    } else {
                        Toast.makeText(requireContext(),getString(R.string.go_to_settings),Toast.LENGTH_LONG).show()
                        requireContext().openSettings()

                    }
                }

                permission.launch(CALL_PHONE)


            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.enter_valid_amount),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
        return binding.root
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        permission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        }
    }

}


