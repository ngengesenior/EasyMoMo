package com.ngengeapps.easymomo.utils

import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.ngengeapps.easymomo.adapters.RecipientsAdapter
import com.ngengeapps.easymomo.models.Account

@BindingAdapter("accounts")
fun bindAccounts(recyclerView: RecyclerView,accounts:LiveData<List<Account>>?){
    accounts?.value?.let {
        (recyclerView.adapter as RecipientsAdapter).apply {
            submitList(it)
        }
    }

}