package com.example.sharedhouse.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

//Defines yet-to-be-completed expenses
data class UnpurchasedExpense(
    var itemName: String = "",
    var sharedWith: List<String> = emptyList(),
    var quantity: Int = 1,
    @ServerTimestamp
    val timeStamp: Timestamp? = null,
    @DocumentId
    var firestoreID: String = ""
)