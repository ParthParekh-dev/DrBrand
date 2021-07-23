package com.parth.dr_brand

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {

    var profileName = MutableLiveData<String>()


    init{
        profileName.value = "Dr. Parth Parekh"
    }


}