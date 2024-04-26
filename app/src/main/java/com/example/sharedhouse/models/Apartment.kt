package com.example.sharedhouse.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class Apartment (
    var apartmentName: String = "",
    var apartmentID: String = "",
    var roomatesIdArray: List<String> = emptyList(),
    @ServerTimestamp
    val timeStamp: Timestamp? = null,
    @DocumentId
    var firestoreID: String = "")
)