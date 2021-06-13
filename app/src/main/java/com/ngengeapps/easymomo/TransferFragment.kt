package com.ngengeapps.easymomo

import android.Manifest.permission.CALL_PHONE
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ngengeapps.easymomo.databinding.FragmentTransferBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TransferFragment : Fragment() {

    private val args: TransferFragmentArgs by navArgs()
    private lateinit var binding: FragmentTransferBinding
    lateinit var permission:ActivityResultLauncher<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTransferBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.account = args.account

        binding.buttonSend.setOnClickListener {
            val amount = binding.editTextAmount.text.toString().trim()
            if (amount.isDigitsOnly()) {

                if (ContextCompat.checkSelfPermission(requireContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                    val intent = Intent(
                        Intent.ACTION_CALL,
                        Uri.fromParts(
                            "tel",
                            "*126*9*${args.account.number}*$amount*1#",
                            null
                        )
                    )
                    startActivity(intent)
                    findNavController().navigate(R.id.navigation_my_qr)


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


fun Context.openSettings() {

    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    val uri = Uri.fromParts("package",packageName,null)
    intent.data = uri
    startActivity(intent)
}