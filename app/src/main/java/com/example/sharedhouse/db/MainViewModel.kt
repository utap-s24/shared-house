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
    private var curUser = FirebaseAuth.getInstance().currentUser
    private var curApartment = MutableLiveData<Apartment>()
    private var total = MutableLiveData<Double>(0.0)
    private var allApartments = MutableLiveData<List<Apartment>>()
    private var allRoomates = MutableLiveData<HashMap<String, String>>()

    init {
        updateCurrentApartment()
    }

    fun observePurchasedItems() = purchasedItems
    fun observeUnpurchasedItems() = unpurchasedItems

    fun observeCurrentApartment() = curApartment

    fun observeAllApartments() = allApartments

    fun observeAllRoomates() = allRoomates


    fun updatePurchasedItems() {
        FirestoreService().dbFetchAllPurchasedExpenses(purchasedItems, curApartment.value!!.firestoreID)

    }

    fun updateUnpurchasedItems() {
        FirestoreService().dbFetchAllUnpurchasedExpenses(unpurchasedItems, curApartment.value!!.firestoreID)

    }

    fun addUnpurchasedExpense(unpurchasedExpense: UnpurchasedExpense) {
        FirestoreService().dbAddUnpurchasedExpense(unpurchasedExpense, curApartment.value!!.firestoreID)
    }

    fun addNewApartment(apartmentId: String) {
        FirestoreService().dbAddNewApartment(apartmentId, curUser!!)
        updateCurrentApartment()
    }

    fun addUserToExistingApartment(apartmentId: String) {
        FirestoreService().dbAddUserToExisitingApartment(curUser!!,apartmentId)
        updateCurrentApartment()
    }

    fun updateCurrentApartment() {
        FirestoreService().dbGetUsersApartmentID(curUser!!, curApartment)
    }

    fun getAllApartments() {
        FirestoreService().dbGetAllAparments(allApartments)
    }

    fun getAllRoomates() {
        FirestoreService().dbGetAllRoomatesNames(allRoomates, curApartment.value!!.firestoreID)
    }


    fun calculateTotalUserOwed() {
        for (item in purchasedItems.value!!) {
            if (item.purchasedBy == curUser!!.uid) {
                // User purchased this item
                // Calculate how much each user owes the purchaser
                total.value = total.value?.plus(item.price)
            } else if (curUser!!.uid in item.sharedWith) {
                // User did not purchase this item, but is sharing the cost
                // Update user's total owed
                total.value = total.value?.minus(item.price / item.sharedWith.size)
            }
        }
        total.postValue(total.value)
    }

    fun getItemMeta(position: Int) : UnpurchasedExpense {
        val item = unpurchasedItems.value?.get(position)
        return item!!
    }

    fun getApartmentMeta(position: Int) : Apartment {
        val apt = allApartments.value?.get(position)
        return apt!!
    }

}