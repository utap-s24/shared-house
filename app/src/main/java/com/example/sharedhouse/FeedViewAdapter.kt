package com.example.sharedhouse

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedhouse.databinding.RowCompletedExpenseBinding
import com.example.sharedhouse.databinding.RowItemBinding
import com.example.sharedhouse.db.MainViewModel
import com.example.sharedhouse.models.PurchasedItem
import com.example.sharedhouse.models.UnpurchasedExpense

class FeedViewAdapter(private val viewModel: MainViewModel, private val navController: NavController )
    : ListAdapter<PurchasedItem, FeedViewAdapter.VH>(Diff()) {
    // This class allows the adapter to compute what has changed
    class Diff : DiffUtil.ItemCallback<PurchasedItem>() {
        override fun areItemsTheSame(oldItem: PurchasedItem, newItem: PurchasedItem): Boolean {
            return oldItem.firestoreID == newItem.firestoreID
        }

        override fun areContentsTheSame(oldItem: PurchasedItem, newItem: PurchasedItem): Boolean {
            if (oldItem.sharedWith.size != newItem.sharedWith.size) {
                return false
            }
            // Check if each element in list1 exists in list2
            for (element in oldItem.sharedWith) {
                if (element !in newItem.sharedWith) {
                    return false
                }
            }
            // Check if each element in list2 exists in list1
            for (element in oldItem.sharedWith) {
                if (element !in newItem.sharedWith) {
                    return false
                }
            }
            return oldItem.firestoreID == newItem.firestoreID
//                    && oldItem.itemName == newItem.itemName
//                    && oldItem.quantity == newItem.quantity
//                    && oldItem.timeStamp == newItem.timeStamp
        }
    }

    inner class VH(private val rowBinding: RowCompletedExpenseBinding) :
        RecyclerView.ViewHolder(rowBinding.root) {

        fun bind(holder: VH, position: Int) {
            //to-do(complete)link up viewModel to get the specific item at position
            Log.d("FeedViewAdapter", "Binding item at position $position")
            val itemMeta = viewModel.getPurchasedItemMeta(position)
            holder.rowBinding.itemName.text = itemMeta.name
            holder.rowBinding.itemPrice.text = itemMeta.price.toString()
            holder.rowBinding.purchaser.text = itemMeta.purchasedBy
            rowBinding.root.setOnClickListener {
                val action = FeedViewFragmentDirections.actionNavigationFeedToCompletedExpenseView(itemMeta.firestoreID)
                navController.navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        Log.d("FeedViewAdapter", "Creating new view holder")
        val rowBinding = RowCompletedExpenseBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return VH(rowBinding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(holder, position)
    }
}