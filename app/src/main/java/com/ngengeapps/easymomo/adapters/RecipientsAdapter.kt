package com.ngengeapps.easymomo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ngengeapps.easymomo.databinding.LayoutContactBinding
import com.ngengeapps.easymomo.models.Account


class RecipientsAdapter(private val clickListener: AccountListener) :
    ListAdapter<Account, AccountViewHolder>(AccountDiffCallback()) {

    class AccountListener(val clickListener: (account: Account) -> Unit) {
        fun onClick(account: Account) = clickListener(account)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        return AccountViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item,clickListener)

    }

}

class AccountViewHolder(
    val binding: LayoutContactBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(account: Account, clickListener: RecipientsAdapter.AccountListener) {
        binding.account = account
        binding.clickListener = clickListener
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): AccountViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = LayoutContactBinding.inflate(layoutInflater)
            return AccountViewHolder(binding)
        }
    }
}


class AccountDiffCallback : DiffUtil.ItemCallback<Account>() {
    override fun areItemsTheSame(oldItem: Account, newItem: Account): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Account, newItem: Account): Boolean {
        return oldItem.number == newItem.number
    }

}