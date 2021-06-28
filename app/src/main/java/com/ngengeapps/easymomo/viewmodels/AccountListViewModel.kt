package com.ngengeapps.easymomo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ngengeapps.easymomo.database.AccountDao
import com.ngengeapps.easymomo.models.Account
import com.ngengeapps.easymomo.models.TrimmedAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountListViewModel @Inject constructor (private val dataSource:AccountDao):ViewModel() {
    private val _accounts:MutableLiveData<List<Account>> = MutableLiveData()
    val accounts:LiveData<List<Account>>
    get() = _accounts
    var selectedAccount:TrimmedAccount = TrimmedAccount("","")

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _accounts.postValue(dataSource.getAccounts())
        }


    }

    fun setAccount(trimmedAccount: TrimmedAccount) {
        selectedAccount = trimmedAccount
    }
    fun insert(account: Account) {
        viewModelScope.launch {
            dataSource.insert(account)
        }
    }

    fun delete(account:Account) {
        viewModelScope.launch {
            dataSource.delete(account)
        }
    }
}