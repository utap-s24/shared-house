package com.example.sharedhouse

import com.google.firebase.firestore.DocumentId

data class ItemMeta (
    var name: String = "",
    var sharedWith: Array<String> = emptyArray(),
    var quantity: Int = 1

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ItemMeta

        if (name != other.name) return false
        if (!sharedWith.contentEquals(other.sharedWith)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + sharedWith.contentHashCode()
        return result
    }
}



import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

// Firebase insists we have a no argument constructor
data class PhotoMeta(
    // Auth information
    var ownerName: String = "",
    var ownerUid: String = "",
    var uuid : String = "",
    var byteSize : Long = 0L,
    var pictureTitle: String = "",
    // Written on the server
    @ServerTimestamp val timeStamp: Timestamp? = null,
    // firestoreID is generated by firestore, used as primary key

)