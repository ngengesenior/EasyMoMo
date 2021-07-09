package com.ngengeapps.easymomo.viewmodels

import androidx.lifecycle.*
import com.ngengeapps.easymomo.database.AccountDao
import com.ngengeapps.easymomo.models.Account
import com.ngengeapps.easymomo.models.TrimmedAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountListViewModel @Inject constructor (private val dataSource:AccountDao):ViewModel() {
    //private val _accounts:MutableLiveData<List<Account>> = MutableLiveData()
    val accounts:LiveData<List<Account>> = dataSource.getAccounts().asLiveData()
    //get() = _accounts
    var selectedAccount:MutableLiveData<TrimmedAccount> = MutableLiveData()


    fun setAccount(trimmedAccount: TrimmedAccount) {
        selectedAccount.value = trimmedAccount
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