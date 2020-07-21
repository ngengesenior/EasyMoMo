package com.ngengeapps.easymomo

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.ngengeapps.easymomo.databinding.FragmentMyQRCodeBinding
import com.ngengeapps.easymomo.utils.GenerateQR
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MyQRCodeFragment : Fragment() {


    private lateinit var binding: FragmentMyQRCodeBinding
    val preferences: SharedPreferences by lazy {
        requireActivity().getSharedPreferences("my_account", MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMyQRCodeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        generateCode()
    }

    private fun generateCode() {
        val details = preferences.getString("MY_ACCOUNT", null)
        if (details != null && details.isNotBlank()) {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                val image = GenerateQR.generateBitmap(
                    details,
                    600,
                    600,
                    4,
                    ContextCompat.getColor(requireContext(), R.color.colorPrimary),
                    Color.parseColor("#FFF5EE")
                )
                withContext(Dispatchers.Main) {
                    binding.imageViewQR.setImageBitmap(image)
                }
            }
        } else {
            Toast.makeText(
                requireActivity(),
                getString(R.string.need_to_set_details),
                Toast.LENGTH_SHORT
            ).show()
        }
        //val details = "%s:%s:%s".format(name,email,phone)


    }
}