package com.ngengeapps.easymomo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor():ViewModel() {
    private val _currentPage = MutableLiveData(0)
    private val _percentProceeded = MutableLiveData(1/3f)
    val percentProceeded:LiveData<Float>
    get() = _percentProceeded
    val currentPage:LiveData<Int>
    get() = _currentPage
    init {
        _currentPage.value = 0
        _percentProceeded.value = 1/3f
    }

    fun advanceForward(){
        if (_currentPage.value!! <= 2) {
           _currentPage.value = _currentPage.value?.plus(1)
            _percentProceeded.value = _percentProceeded.value?.plus(1/3f)
        }
    }

    fun advanceBackward(){
        if (_currentPage.value!! >= 0) {
            _currentPage.value = _currentPage.value?.minus(1)
            _percentProceeded.value = _percentProceeded.value?.minus(1/3f)
        }
    }

}