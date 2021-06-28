package com.ngengeapps.easymomo.viewmodels

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.ngengeapps.easymomo.models.TrimmedAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import com.ngengeapps.easymomo.R

private const val TAG = "Your Details"
@HiltViewModel
class YourDetailsViewModel @Inject constructor( @ApplicationContext private val context:Context,
                                                private val gson: Gson,private val preferences: SharedPreferences):ViewModel() {
    private val _isEditing:MutableLiveData<Boolean> = MutableLiveData(false)
    val isEditing:LiveData<Boolean>
    get() = _isEditing
    private val _name = MutableLiveData<String>()
    val name:LiveData<String>
    get() = _name
    private val _number = MutableLiveData<String>()
    val number:LiveData<String>
    get() = _number
    var errorMessage = MutableLiveData("")
    private val _account = MutableLiveData<TrimmedAccount>(null)
    val account:LiveData<TrimmedAccount>
    get() = _account
    init {
        val details = preferences.getString("MY_ACCOUNT",null)
        if (details != null && details.isNotBlank()) {
            _account.value = gson.fromJson(details,TrimmedAccount::class.java)
            _account.value?.let {
                updateName(it.name)
                updateNumber(it.number)
            }
        } else {
            Log.e(TAG, "No account in pref: ", )
        }
    }




    fun updateYourDetails() {
        updateIsEditing(true)
        if (_name.value!!.trim().isNotEmpty() && _number.value!!.trim().isNotEmpty()) {
            val account = TrimmedAccount(_name.value!!.trim(), _number.value!!.trim())
            with(preferences.edit()) {
                putString("MY_ACCOUNT", gson.toJson(account,
                    TrimmedAccount::class.java))
                commit()
                updateIsEditing(false)

            }
        } else {
            errorMessage.value = context.getString(R.string.fields_required)
        }

    }

    fun updateName(nm:String) {
        _name.value = nm
    }

    fun updateNumber(num:String){
        _number.value = num
    }


    fun updateIsEditing(value:Boolean) {
        _isEditing.value = value
    }



}