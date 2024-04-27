package com.example.sharedhouse

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedhouse.databinding.RowItemBinding
import com.example.sharedhouse.db.MainViewModel
import com.example.sharedhouse.models.UnpurchasedExpense

class ItemListAdapter(private val viewModel: MainViewModel, private val navController: NavController )
    : ListAdapter<UnpurchasedExpense, ItemListAdapter.VH>(Diff()) {
    // This class allows the adapter to compute what has changed
    class Diff : DiffUtil.ItemCallback<UnpurchasedExpense>() {
        override fun areItemsTheSame(oldItem: UnpurchasedExpense, newItem: UnpurchasedExpense): Boolean {
            return oldItem.firestoreID == newItem.firestoreID
        }

        override fun areContentsTheSame(oldItem: UnpurchasedExpense, newItem: UnpurchasedExpense): Boolean {
            if (oldItem.sharedWith.size != newItem.sharedWith.size) {
                return false
            }
            for (element in oldItem.sharedWith) {
                if (element !in newItem.sharedWith) {
                    return false
                }
            }

            for (element in oldItem.sharedWith) {
                if (element !in newItem.sharedWith) {
                    return false
                }
            }
            return oldItem.firestoreID == newItem.firestoreID
                    && oldItem.itemName == newItem.itemName
                    && oldItem.quantity == newItem.quantity
                    && oldItem.timeStamp == newItem.timeStamp
        }
    }

    inner class VH(private val rowBinding: RowItemBinding) :
        RecyclerView.ViewHolder(rowBinding.root) {

        fun bind(holder: VH, position: Int) {
            val itemMeta = viewModel.getItemMeta(position)
            holder.rowBinding.itemName.text = itemMeta.itemName
            holder.rowBinding.itemQuantity.text = itemMeta.quantity.toString()
            rowBinding.root.setOnClickListener {
                val action = ListFragmentDirections.actionListFragmentToCompleteExpense(position)
                navController.navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val rowBinding = RowItemBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return VH(rowBinding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(holder, position)
    }
}