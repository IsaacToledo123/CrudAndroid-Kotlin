package com.example.nuevocomienzo.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomViewModel: ViewModel() {
    private var _number= MutableLiveData<Int>()
    val number : LiveData<Int> =_number
    fun onChangedNumber(){
        _number.value=_number.value!!+1 //el operador !! es por que estamos seguros que no va a ver un nulo

    }
}