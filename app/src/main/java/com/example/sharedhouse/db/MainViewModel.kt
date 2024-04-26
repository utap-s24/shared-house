package com.example.sharedhouse.db

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private var purchasedItems = MutableLiveData("Uninitialized")
    private var longTimeExpenses = MutableLiveData("Uninitialized")
    private var unpurchasedItems = MutableLiveData("Uninitialized")

}