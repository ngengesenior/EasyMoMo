package com.ngengeapps.easymomo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ngengeapps.easymomo.databinding.FragmentAddRecipientBinding
import com.ngengeapps.easymomo.models.Account
import com.ngengeapps.easymomo.viewmodels.AccountListViewModel
import com.ngengeapps.easymomo.viewmodels.AccountListViewModelFactory


class AddRecipientFragment : Fragment() {
    private lateinit var binding:FragmentAddRecipientBinding
    val accountsViewModel: AccountListViewModel by activityViewModels {
        AccountListViewModelFactory(activity as Context)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddRecipientBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = accountsViewModel
        binding.buttonSave.setOnClickListener {
            binding.apply {
                val name = name.text.toString().trim()
                val number = phone.text.toString().trim()

                viewModel?.insert(Account(name = name,number = number))
                findNavController().popBackStack()
            }
        }
        return binding.root
    }


}