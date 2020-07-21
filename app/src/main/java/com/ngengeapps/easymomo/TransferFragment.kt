package com.ngengeapps.easymomo

import android.Manifest.permission.CALL_PHONE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ngengeapps.easymomo.databinding.FragmentTransferBinding


class TransferFragment : Fragment() {

    private val args: TransferFragmentArgs by navArgs()
    private lateinit var binding: FragmentTransferBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTransferBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.account = args.account
        binding.buttonSend.setOnClickListener {
            val amount = binding.editTextAmount.text.toString().trim()
            if (amount.isDigitsOnly()) {

                val permission =
                    registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted: Boolean ->
                        if (granted) {
                            Log.d("TAG--",args.account.number)
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
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.permission_momo_required),
                                Toast.LENGTH_SHORT
                            ).show()
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

}