package com.example.sharedhouse.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class PurchasedItem(
    var name: String = "",
    var price: Double = 0.0,
    var hasPaid: HashMap<String, Boolean> = hashMapOf(),
    var purchasedBy: String = "",
    var quantity: Int = 0,
    //Hashmap mapping user id to comment
    var comments: List<HashMap<String, String>> = emptyList(),
    @ServerTimestamp val timeStamp: Timestamp? = null,
    @DocumentId var firestoreID: String = ""
)