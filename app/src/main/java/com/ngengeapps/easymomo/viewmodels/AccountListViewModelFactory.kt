package com.ngengeapps.easymomo.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ngengeapps.easymomo.database.AccountDatabase

class AccountListViewModelFactory(val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AccountListViewModel(AccountDatabase.getInstance(context).accountDao) as T
    }


}