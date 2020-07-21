package com.ngengeapps.easymomo

import android.Manifest.permission.CALL_PHONE
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ngengeapps.easymomo.adapters.RecipientsAdapter
import com.ngengeapps.easymomo.databinding.FragmentRecipientsListBinding
import com.ngengeapps.easymomo.models.Account
import com.ngengeapps.easymomo.viewmodels.AccountListViewModel
import com.ngengeapps.easymomo.viewmodels.AccountListViewModelFactory


class RecipientsListFragment : Fragment() {

    val accountsViewModel: AccountListViewModel by activityViewModels {
        AccountListViewModelFactory(activity as Context)
    }

    private lateinit var binding: FragmentRecipientsListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRecipientsListBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = accountsViewModel


        binding.recyclerView.setHasFixedSize(true)

        binding.recyclerView.adapter = RecipientsAdapter(RecipientsAdapter.AccountListener {

            val action =
                RecipientsListFragmentDirections.actionNavigationReceiversToNavigationTransfer(it.trimAccount())
            findNavController().navigate(action)
        })
        binding.buttonAddContact.setOnClickListener {
            /*val action = RecipientsListFragmentDirections.actionNavigationReceiversToNavigationAddRecipient()
            findNavController().navigate(action)*/
            val action =
                RecipientsListFragmentDirections.actionNavigationReceiversToNavigationAddRecipient()
            findNavController().navigate(action)

        }
        return binding.root
    }
}


