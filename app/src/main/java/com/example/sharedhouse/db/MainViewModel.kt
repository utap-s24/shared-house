package com.example.sharedhouse.db

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sharedhouse.models.PurchasedItem
import com.example.sharedhouse.models.UnpurchasedExpense
import com.google.firebase.auth.FirebaseAuth

class MainViewModel : ViewModel() {
    private var purchasedItems = MutableLiveData<List<PurchasedItem>>()
    private var unpurchasedItems = MutableLiveData<List<UnpurchasedExpense>>()
    private var curUser = FirebaseAuth.getInstance().currentUser
    var aptId : String = ""
    private var total = MutableLiveData<Double>(0.0)

    fun observePurchasedItems() = purchasedItems
    fun observeUnpurchasedItems() = unpurchasedItems


    fun updatePurchasedItems() {
        FirestoreService().dbFetchAllPurchasedExpenses(purchasedItems, aptId)

    }

    fun updateUnpurchasedItems() {
        FirestoreService().dbFetchAllUnpurchasedExpenses(unpurchasedItems, aptId)
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


}