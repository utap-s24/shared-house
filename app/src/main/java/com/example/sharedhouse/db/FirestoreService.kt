package com.example.sharedhouse.db
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject


class Roomate {
    var name: String = ""
    var email: String = ""
    var uid: String = ""

}

class FirestoreService {
    private val db = FirebaseFirestore.getInstance()

//
//    fun dbFetchApartmentRoomates: MutableLiveData<List<Roomate>>,
//                             callback:()->Unit = {}) {
//        db.collection("apartments")
//            .orderBy("timeStamp", Query.Direction.DESCENDING)
//            .limit(100)
//            .get()
//            .addOnSuccessListener { result ->
//                Log.d(javaClass.simpleName, "allNotes fetch ${result!!.documents.size}")
//                // NB: This is done on a background thread
//                notesList.postValue(result.documents.mapNotNull {
//                    it.toObject(Note::class.java)
//                })
//                callback()
//            }
//            .addOnFailureListener {
//                Log.d(javaClass.simpleName, "allNotes fetch FAILED ", it)
//                callback()
//            }
//    }

    // Add more methods as needed for other Firestore operations
}
