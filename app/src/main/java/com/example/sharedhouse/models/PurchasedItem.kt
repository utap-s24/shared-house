package com.example.sharedhouse.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

//class PurchasedItem {
//    var name: String = ""
//    var price: Double = 0.0
//    var sharedWith: List<String> = emptyList()
//    var purchasedBy: String = ""
//    var quantity = 0
//    var picture_uuid: String  = ""
//    var documentId: String = ""
//}
data class PurchasedItem(
    var name: String = "",
    var price: Double = 0.0,
    var sharedWith: List<String> = emptyList(),
    var purchasedBy: String = "",
    var quantity: Int = 0,
    //Hashmap mapping user id to comment
    var comments: HashMap<String, String> = hashMapOf(),
    @ServerTimestamp val timeStamp: Timestamp? = null,
    @DocumentId var firestoreID: String = "")