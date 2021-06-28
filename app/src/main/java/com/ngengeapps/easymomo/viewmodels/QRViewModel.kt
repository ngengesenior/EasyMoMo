package com.ngengeapps.easymomo.viewmodels

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ngengeapps.easymomo.R
import com.ngengeapps.easymomo.utils.GenerateQR
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
const val TAG_ = "SCANNER"
@HiltViewModel
class QRViewModel @Inject constructor(@ApplicationContext val context: Context, private val prefs:SharedPreferences) : ViewModel(){
    private val _bitmap = MutableLiveData<Bitmap>(null)
    val bitmap: LiveData<Bitmap>
    get() = _bitmap

    init {
        val account = prefs.getString("MY_ACCOUNT",null)
        Log.d(TAG_, ": $account")
        if (account != null && account.isNotEmpty()) {
            generateBitMap(account)
        }
    }


    private fun generateBitMap(string: String) {
        viewModelScope.launch(Dispatchers.IO) {
           _bitmap.postValue(
               GenerateQR.generateBitmap(
                string,
                600,
                600,
                4,
                ContextCompat.getColor(context, R.color.colorPrimary),
                Color.parseColor("#FFF5EE")
            ))
        }
    }

}