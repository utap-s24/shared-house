package com.example.sharedhouse

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedhouse.databinding.RowCommentBinding
import com.example.sharedhouse.databinding.RowCompletedExpenseBinding
import com.example.sharedhouse.databinding.RowItemBinding
import com.example.sharedhouse.db.MainViewModel
import com.example.sharedhouse.models.PurchasedItem
import com.example.sharedhouse.models.UnpurchasedExpense

class CompletedExpenseViewAdapter(private val viewModel: MainViewModel, private val navController: NavController )
    : ListAdapter<PurchasedItem, CompletedExpenseViewAdapter.VH>(Diff()) {
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

    inner class VH(private val rowBinding: RowCommentBinding) :
        RecyclerView.ViewHolder(rowBinding.root) {

        fun bind(holder: VH, position: Int) {
            //TODO: link up viewModel to get the specific item at position
            val itemMeta = PurchasedItem()
            //TODO: attach comment and name to view binding.
//            holder.rowBinding.itemName.text =
//            holder.rowBinding.commenter.text =
            // if the id of current user == id of the commenter:
//              holder.rowBinding.layout.setHorizontalGravity(Gravity.RIGHT)
            //else {
//              holder.rowBinding.layout.setHorizontalGravity(Gravity.RIGHT)
//          }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val rowBinding = RowCommentBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return VH(rowBinding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(holder, position)
    }
}