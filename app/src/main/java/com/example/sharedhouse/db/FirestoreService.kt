package com.example.sharedhouse.db

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.sharedhouse.models.PurchasedItem
import com.example.sharedhouse.models.UnpurchasedExpense
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
    private val collectionRoot = "apartments"

    fun dbFetchAllUnpurchasedExpenses(
        unpurchasedExpenseList: MutableLiveData<List<UnpurchasedExpense>>,
        curUserApartmentID: String,
    ) {
        db.collection(collectionRoot)
            .document(curUserApartmentID)
            .collection("unpurchased_expenses")
            .get()
            .addOnSuccessListener { result ->
                Log.d(javaClass.simpleName, "all unpurchased expense fetch ${result!!.documents.size}")
                // NB: This is done on a background thread
                unpurchasedExpenseList.postValue(result.documents.mapNotNull {
                    it.toObject(UnpurchasedExpense::class.java)
                })
            }
            .addOnFailureListener {
                Log.d(javaClass.simpleName, "all unpurchased fetch FAILED ", it)
            }
    }


    fun dbFetchAllPurchasedExpenses(
        purchasedExpenseList: MutableLiveData<List<PurchasedItem>>,
        curUserApartmentID: String,
    ) {
        db.collection(collectionRoot)
            .document(curUserApartmentID)
            .collection("completed_expenses")
            .get()
            .addOnSuccessListener { result ->
                Log.d(javaClass.simpleName, "all purchased expense fetch ${result!!.documents.size}")
                // NB: This is done on a background thread
                purchasedExpenseList.postValue(result.documents.mapNotNull {
                    it.toObject(PurchasedItem::class.java)
                })
            }
            .addOnFailureListener {
                Log.d(javaClass.simpleName, "all purchased fetch FAILED ", it)
            }
    }




    fun doMoveFromUnpurchasedToPurchased(
        unpurchasedExpense: UnpurchasedExpense,
        curUserApartmentID: String,
    ) {
        db.collection(collectionRoot)
            .document(curUserApartmentID)
            .collection("unpurchased_expenses")
            .document(unpurchasedExpense.firestoreID)
            .delete()
            .addOnSuccessListener {
                Log.d(javaClass.simpleName, "Unpurchased expense delete \"${unpurchasedExpense.itemName}\"")
                db.collection(collectionRoot)
                    .document(curUserApartmentID)
                    .collection("completed_expenses")
                    .add(unpurchasedExpense)
                    .addOnSuccessListener {
                        Log.d(javaClass.simpleName, "Purchased expense create \"${unpurchasedExpense.itemName}\"")
                    }
                    .addOnFailureListener { e ->
                        Log.d(javaClass.simpleName, "Purchased expense create FAILED \"${unpurchasedExpense.itemName}\"")
                        Log.w(javaClass.simpleName, "Error ", e)
                    }
            }
            .addOnFailureListener { e ->
                Log.d(javaClass.simpleName, "Unpurchased expense delete FAILED \"${unpurchasedExpense.itemName}\"")
                Log.w(javaClass.simpleName, "Error ", e)
            }
    }

    fun doAddUserToPeopleAndApartment (
        user: FirebaseUser,
        apartmentID: String,
    )
    {
        db.collection(collectionRoot)
            .document(apartmentID)
            .collection("roomates")
            .document(user.uid)
            .set({ "name" to user.displayName })
            .addOnSuccessListener {
                Log.d(javaClass.simpleName, "User added to apartment")
            }
            .addOnFailureListener { e ->
                Log.d(javaClass.simpleName, "User add to apartment FAILED")
                Log.w(javaClass.simpleName, "Error ", e)
            }

        db.collection("people")
            .document(user.uid)
            .set({ "name" to user.displayName })
            .addOnSuccessListener {
                Log.d(javaClass.simpleName, "User added to people")
            }
            .addOnFailureListener { e ->
                Log.d(javaClass.simpleName, "User add to people FAILED")
                Log.w(javaClass.simpleName, "Error ", e)
            }
    }

    // Add more methods as needed for other Firestore operations
}