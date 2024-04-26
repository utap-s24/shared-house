package com.example.sharedhouse

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class ItemMeta (
    var name: String = "",
    var sharedWith: Array<String> = emptyArray(),
    var quantity: Int = 1,
    // Written on the server
    @ServerTimestamp val timeStamp: Timestamp? = null,
    @DocumentId var firestoreID: String = ""
)
//{
//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (javaClass != other?.javaClass) return false
//
//        other as ItemMeta
//
//        if (name != other.name) return false
//        if (!sharedWith.contentEquals(other.sharedWith)) return false
//        if (quantity != other.quantity) return false
//        if (timeStamp != other.timeStamp) return false
//        if (firestoreID != other.firestoreID) return false
//
//        return true
//    }
//
//    override fun hashCode(): Int {
//        var result = name.hashCode()
//        result = 31 * result + sharedWith.contentHashCode()
//        result = 31 * result + quantity
//        result = 31 * result + (timeStamp?.hashCode() ?: 0)
//        result = 31 * result + firestoreID.hashCode()
//        return result
//    }
//}