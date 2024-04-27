package com.example.sharedhouse.db

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sharedhouse.models.Apartment
import com.example.sharedhouse.models.PurchasedItem
import com.example.sharedhouse.models.UnpurchasedExpense
import com.google.firebase.auth.FirebaseAuth

class MainViewModel : ViewModel() {
    private var purchasedItems = MutableLiveData<List<PurchasedItem>>()
    private var unpurchasedItems = MutableLiveData<List<UnpurchasedExpense>>()
    private var curApartment = MutableLiveData<Apartment>()
    private var total = MutableLiveData<HashMap<String, Double>>()
    private var allApartments = MutableLiveData<List<Apartment>>()
    private var allRoomates = MutableLiveData<HashMap<String, String>>()

    init {
        curApartment.observeForever { apartment ->
            getAllRoomates()

        }
    }

    fun observePurchasedItems() = purchasedItems
    fun observeUnpurchasedItems() = unpurchasedItems

    fun observeCurrentApartment() = curApartment

    fun observeAllApartments() = allApartments

    fun observeAllRoomates() = allRoomates

    fun observeTotal() = total


    fun updatePurchasedItems() {
        FirestoreService().dbFetchAllPurchasedExpenses(purchasedItems, curApartment.value!!.firestoreID) {
//            calculateTotals()
        }

    }

    fun updateUnpurchasedItems() {
        FirestoreService().dbFetchAllUnpurchasedExpenses(unpurchasedItems, curApartment.value!!.firestoreID)

    }

    fun addUnpurchasedExpense(unpurchasedExpense: UnpurchasedExpense) {
        FirestoreService().dbAddUnpurchasedExpense(unpurchasedExpense, curApartment.value!!.firestoreID)
    }

    fun addNewApartment(apartmentId: String) {
        FirestoreService().dbAddNewApartment(apartmentId, FirebaseAuth.getInstance().currentUser!!) {
            updateCurrentApartment()
        }

    }

    fun addUserToExistingApartment(apartmentId: String) {
        FirestoreService().dbAddUserToExisitingApartment(FirebaseAuth.getInstance().currentUser!!,apartmentId) {
            updateCurrentApartment()
        }

    }

    fun updateCurrentApartment() {
        FirestoreService().dbGetUsersApartmentID(FirebaseAuth.getInstance().currentUser!!, curApartment) {

        }
    }


    fun getAllApartments() {
        FirestoreService().dbGetAllAparments(allApartments)
    }

    fun getRoomatesWithoutObserving(): HashMap<String, String> {
        return allRoomates.value!!
    }

    fun getAllRoomates() {
        FirestoreService().dbGetAllRoomatesNames(allRoomates, curApartment.value!!.firestoreID) {
            updateUnpurchasedItems()
            updatePurchasedItems()
        }
    }

    fun addPurchasedItem(unpurchasedExpense: UnpurchasedExpense, amount: Double, comment: String) {

        var commentList = ArrayList<HashMap<String, String>>()
        var map = HashMap<String, String>()
        map["name"] = FirebaseAuth.getInstance().currentUser!!.displayName!!
        map["comment"] = comment
        if (comment.isNotEmpty()){
            commentList.add(map)
        }

        FirestoreService().doMoveFromUnpurchasedToPurchased(unpurchasedExpense, amount, commentList, curApartment.value!!.firestoreID, FirebaseAuth.getInstance().currentUser!!){

            val createdPurchaseItem = it
            val updatedApartment = curApartment.value?.apply {
                // Update fields in the apartment as necessary
                // For example, you might want to remove the expense from a list of unpurchased expenses
                this.unpurchasedExpenses.remove(unpurchasedExpense)
                this.purchasedItems.add(createdPurchaseItem)
                // If you have a list of purchased expenses, you might want to add it there, and so on
            }
            // Post the updated value to the MutableLiveData
            curApartment.postValue(updatedApartment!!)
//            calculateTotals()
        }
//

    }

    fun updateAllApartmentExpenses() {
        FirestoreService().dbUpdateAllApartmentExpenses(curApartment.value!!.firestoreID) {
            val updatedApartment = it
            curApartment.postValue(updatedApartment)
        }
    }


    fun addCommentToPurchasedItem(purchasedItem: PurchasedItem, comment: String) {
        var map = HashMap<String, String>()
        map["name"] = FirebaseAuth.getInstance().currentUser!!.displayName!!
        map["comment"] = comment
        FirestoreService().dbAddCommentToPurchasedItem(purchasedItem, map, curApartment.value!!.firestoreID){
          updatePurchasedItems()
        }
    }


    fun addPurchasedExpense (purchasedItem: PurchasedItem) {
        FirestoreService().dbAddPurchasedExpense(purchasedItem, curApartment.value!!.firestoreID)
    }


    fun updateHasPaid(purchasedItem: PurchasedItem) {
        val map = purchasedItem.hasPaid
        map[FirebaseAuth.getInstance().currentUser!!.uid] = true
        purchasedItem.hasPaid = map


        FirestoreService().dbChangeHasPaidValue(purchasedItem, curApartment.value!!.firestoreID, ){
            updatePurchasedItems()
        }
    }


    fun calculateTotals() {
        var curUserId = FirebaseAuth.getInstance().currentUser!!.uid
        var mapOfIdToOwed = HashMap<String, Double>()
        for (id in allRoomates.value!!.keys) {
            mapOfIdToOwed[id] = 0.0
        }
        for (purchasedItem in purchasedItems.value!!) {
            for (id in purchasedItem.hasPaid.keys) {
                if (purchasedItem.hasPaid[id] == false) {
                    if(purchasedItem.purchasedBy == curUserId) {
                        mapOfIdToOwed[id] = mapOfIdToOwed[id]!! - purchasedItem.price / purchasedItem.hasPaid.size
                    }
                    else if (id == curUserId) {
                        mapOfIdToOwed[purchasedItem.purchasedBy] = mapOfIdToOwed[purchasedItem.purchasedBy]!! + purchasedItem.price / purchasedItem.hasPaid.size
                    }

                }
            }

        }

        var netSum = 0.0
        for (id in mapOfIdToOwed.keys) {
            netSum += mapOfIdToOwed[id]!!
        }
        mapOfIdToOwed[curUserId] = netSum

        total.postValue(mapOfIdToOwed)

//        total.postValue(total.value)
    }



    fun getItemMeta(position: Int) : UnpurchasedExpense {
        val item = unpurchasedItems.value?.get(position)
        return item!!
    }

    fun getApartmentMeta(position: Int) : Apartment {
        val apt = allApartments.value?.get(position)
        return apt!!
    }

    fun getPurchasedItemMeta(position: Int) : PurchasedItem {
        val item = purchasedItems.value?.get(position)
        return item!!
    }

}