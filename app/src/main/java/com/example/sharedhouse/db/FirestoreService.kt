package com.example.sharedhouse.db

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.sharedhouse.models.Apartment
import com.example.sharedhouse.models.PurchasedItem
import com.example.sharedhouse.models.UnpurchasedExpense
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

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
        callback: () -> Unit
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

                result.documents.mapNotNull {
                    Log.d(javaClass.simpleName, "all purchased expense fetch ${it.toObject(PurchasedItem::class.java)}")
                 }

                callback()
            }
            .addOnFailureListener {
                Log.d(javaClass.simpleName, "all purchased fetch FAILED ", it)
            }
    }

    //Function to change the hasPaid value of FireBase user in a purchased item.
    fun dbChangeHasPaidValue(
        purchasedItem: PurchasedItem,
        curUserApartmentID: String,
        callback: () -> Unit
    ) {
        db.collection(collectionRoot)
            .document(curUserApartmentID)
            .collection("completed_expenses")
            .document(purchasedItem.firestoreID)
            .update("hasPaid", purchasedItem.hasPaid)
            .addOnSuccessListener {
                Log.d(javaClass.simpleName, "User has paid")
                callback()
            }
            .addOnFailureListener { e ->
                Log.d(javaClass.simpleName, "User has paid FAILED")
                Log.w(javaClass.simpleName, "Error ", e)
            }
    }

    fun dbAddPurchasedExpense(
        purchasedItem: PurchasedItem,
        curUserApartmentID: String,
    ) {
        db.collection(collectionRoot)
            .document(curUserApartmentID)
            .collection("completed_expenses")
            .add(purchasedItem)
            .addOnSuccessListener {
                Log.d(javaClass.simpleName, "Purchased expense create \"${purchasedItem.name}\"")
            }
            .addOnFailureListener { e ->
                Log.d(javaClass.simpleName, "Purchased expense create FAILED \"${purchasedItem.name}\"")
                Log.w(javaClass.simpleName, "Error ", e)
            }
    }

    fun dbAddUnpurchasedExpense(
        unpurchasedExpense: UnpurchasedExpense,
        curUserApartmentID: String,
    ) {
        db.collection(collectionRoot)
            .document(curUserApartmentID)
            .collection("unpurchased_expenses")
            .add(unpurchasedExpense)
            .addOnSuccessListener {
                Log.d(javaClass.simpleName, "Unpurchased expense create \"${unpurchasedExpense.itemName}\"")
            }
            .addOnFailureListener { e ->
                Log.d(javaClass.simpleName, "Unpurchased expense create FAILED \"${unpurchasedExpense.itemName}\"")
                Log.w(javaClass.simpleName, "Error ", e)
            }
    }

    fun doMoveFromUnpurchasedToPurchased(
        unpurchasedExpense: UnpurchasedExpense,
        amount: Double,
        commentMap: List<HashMap<String, String>>,
        curUserApartmentID: String,
        curUser: FirebaseUser,
        callback: (PurchasedItem) -> Unit
    ) {
        db.collection(collectionRoot)
            .document(curUserApartmentID)
            .collection("unpurchased_expenses")
            .document(unpurchasedExpense.firestoreID)
            .delete()
            .addOnSuccessListener {
                Log.d(javaClass.simpleName, "Unpurchased expense delete \"${unpurchasedExpense.itemName}\"")

                val paidHashMapped = HashMap<String, Boolean>()
                for (roomate in unpurchasedExpense.sharedWith) {
                    paidHashMapped[roomate] = false
                }
                if (curUser.uid in unpurchasedExpense.sharedWith) {
                    paidHashMapped[curUser.uid] = true
                }
                db.collection(collectionRoot)
                    .document(curUserApartmentID)
                    .collection("completed_expenses")
                    .add(PurchasedItem(unpurchasedExpense.itemName, amount, paidHashMapped, curUser.uid, unpurchasedExpense.quantity, commentMap))
                    .addOnSuccessListener {
                        var newPurchasedExpense = PurchasedItem(unpurchasedExpense.itemName, amount, paidHashMapped, curUser.uid, unpurchasedExpense.quantity, commentMap, firestoreID = it.id )
                        callback(newPurchasedExpense)
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

    //Function to add a comment to a purchased item
    fun dbAddCommentToPurchasedItem(
        purchasedItem: PurchasedItem,
        commentMap: HashMap<String, String>,
        curUserApartmentID: String,
        callback: (PurchasedItem) -> Unit
    ) {
        db.collection(collectionRoot)
            .document(curUserApartmentID)
            .collection("completed_expenses")
            .document(purchasedItem.firestoreID)
            .update("comments", purchasedItem.comments.plus(commentMap))
            .addOnSuccessListener {
                Log.d(javaClass.simpleName, "Comment added to purchased item")
                val updatedPurchasedItem = purchasedItem.apply {
                    comments = comments.plus(commentMap)
                }
                callback(updatedPurchasedItem)
            }
            .addOnFailureListener { e ->
                Log.d(javaClass.simpleName, "Comment add to purchased item FAILED")
                Log.w(javaClass.simpleName, "Error ", e)
            }
    }

    //Add a new apartment upon user request
    fun dbAddNewApartment(
        apartmentName: String,
        curUser: FirebaseUser,
        callback: () -> Unit
    ) {
        val newApartment = hashMapOf(
            "name" to apartmentName,
            "roomates" to List<String>(1) { curUser.uid },
        )
        db.collection(collectionRoot)
            .add(newApartment)
            .addOnSuccessListener {
                Log.d(javaClass.simpleName, "Apartment create \"${apartmentName}\"")


                db.collection("people")
                    .document(curUser.uid)
                    .set(hashMapOf("name" to curUser.displayName, "apartmentId" to it.id))
                    .addOnSuccessListener {
                        Log.d(javaClass.simpleName, "User added to people")
                        callback()
                    }
                    .addOnFailureListener { e ->
                        Log.d(javaClass.simpleName, "User add to people FAILED")
                        Log.w(javaClass.simpleName, "Error ", e)
                    }
            }
            .addOnFailureListener { e ->
                Log.d(javaClass.simpleName, "Apartment create FAILED \"${apartmentName}\"")
                Log.w(javaClass.simpleName, "Error ", e)
            }

    }

    fun dbGetUsersApartmentID(
        user: FirebaseUser,
        apartmentToUpdate: MutableLiveData<Apartment>,
        callback: () -> Unit
    ) {
        db.collection("people")
            .document(user.uid)
            .get()
            .addOnSuccessListener { result ->
                Log.d(javaClass.simpleName, "User people fetch ${result}")
                Log.d(javaClass.simpleName, "User people fetch ${result!!.data}")
                if (result.data == null) {
                    return@addOnSuccessListener

                }
                val apartmentID = result.data!!["apartmentId"] as String
                db.collection(collectionRoot)
                    .document(apartmentID)
                    .get()
                    .addOnSuccessListener { apartmentResult ->
                        Log.d(javaClass.simpleName, "User apartment fetch ${apartmentResult}")
                        Log.d(javaClass.simpleName, "User apartment fetch ${apartmentResult!!.data}")
                        if (apartmentResult.data == null) {
                            return@addOnSuccessListener
                        }

                        val apartmentName = apartmentResult.data!!["name"] as String
                        val roomates = apartmentResult.data!!["roomates"] as List<String>
                        val newApt = Apartment(apartmentName, roomates, firestoreID = apartmentResult.id)
                        apartmentToUpdate.postValue(newApt)
                        callback()
                    }
                    .addOnFailureListener {
                        Log.d(javaClass.simpleName, "User apartment fetch FAILED ", it)
                    }
            }
            .addOnFailureListener {
                Log.d(javaClass.simpleName, "User apartment fetch FAILED ", it)
            }
    }

    //Upon user clicking join button, this function is called
    fun dbAddUserToExisitingApartment (
        user: FirebaseUser,
        apartmentID: String,
        callback: () -> Unit
    )
    {
        db.collection(collectionRoot)
            .document(apartmentID)
            .get()
            .addOnSuccessListener {
                val roomates = it.data!!["roomates"] as List<String>
                val newList : MutableList<String> = roomates.toMutableList()
                newList.add(user.uid)
                 db.collection(collectionRoot)
                    .document(apartmentID)
                    .update("roomates", newList)
                    .addOnSuccessListener {
                        callback()
                    }
                    .addOnFailureListener { e ->
                        Log.d(javaClass.simpleName, "User add to apartment FAILED")
                        Log.w(javaClass.simpleName, "Error ", e)
                    }

            }
            .addOnFailureListener { e ->
                Log.d(javaClass.simpleName, "User add to apartment FAILED")
                Log.w(javaClass.simpleName, "Error ", e)
            }

        db.collection("people")
            .document(user.uid)
            .set(hashMapOf("name" to user.displayName, "apartmentId" to apartmentID))
            .addOnSuccessListener {
                Log.d(javaClass.simpleName, "User added to people")
            }
            .addOnFailureListener { e ->
                Log.d(javaClass.simpleName, "User add to people FAILED")
                Log.w(javaClass.simpleName, "Error ", e)
            }
    }

    fun dbGetAllAparments(
        allApartments: MutableLiveData<List<Apartment>>
    ) {
        val apartments = mutableListOf<Apartment>()
        db.collection(collectionRoot)
            .get()
            .addOnSuccessListener { result ->
                Log.d(javaClass.simpleName, "all apartments fetch ${result!!.documents.size}")
                // NB: This is done on a background thread
                apartments.addAll(result.documents.mapNotNull {
                    it.toObject(Apartment::class.java)
                })
                Log.d(javaClass.simpleName, "apartments: $apartments")
                allApartments.postValue(apartments)
            }
            .addOnFailureListener {
                Log.d(javaClass.simpleName, "all apartments fetch FAILED ", it)
            }

    }

    fun dbGetAllRoomatesNames(
        allRoomates: MutableLiveData<HashMap<String, String>>,
        apartmentID: String,
        callback: () -> Unit
    ) {
        db.collection("people")
            .whereEqualTo("apartmentId", apartmentID)
            .get()
            .addOnSuccessListener { querySnapshot ->
                Log.d(javaClass.simpleName, "all roomates fetch successful")
                val roomatesMap = HashMap<String, String>()
                for (document in querySnapshot.documents) {
                    val userId = document.id
                    val userName = document.getString("name") ?: "Unknown"
                    roomatesMap[userId] = userName
                }
                allRoomates.postValue(roomatesMap)
                callback()
            }
            .addOnFailureListener { exception ->
                Log.d(javaClass.simpleName, "all roomates fetch FAILED", exception)
            }
    }
}
