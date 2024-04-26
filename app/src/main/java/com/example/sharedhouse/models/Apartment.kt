package com.example.sharedhouse.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class Apartment (
    var name: String = "",
    var roomates: List<String> = emptyList(),
    @ServerTimestamp
    val timeStamp: Timestamp? = null,
    @DocumentId
    var firestoreID: String = "",
    var unpurchasedExpenses: MutableList<UnpurchasedExpense> = emptyList<UnpurchasedExpense>().toMutableList(), // Not a Firestore field
    var purchasedItems: MutableList<PurchasedItem> = emptyList<PurchasedItem>().toMutableList()// Not a Firestore field

)