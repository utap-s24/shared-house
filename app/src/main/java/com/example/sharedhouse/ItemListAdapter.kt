package com.example.sharedhouse

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedhouse.databinding.RowItemBinding
import com.example.sharedhouse.db.MainViewModel

class ItemListAdapter(private val viewModel: MainViewModel, private val navController: NavController )
    : ListAdapter<ItemMeta, ItemListAdapter.VH>(Diff()) {
    // This class allows the adapter to compute what has changed
    class Diff : DiffUtil.ItemCallback<ItemMeta>() {
        override fun areItemsTheSame(oldItem: ItemMeta, newItem: ItemMeta): Boolean {
            return oldItem.firestoreID == newItem.firestoreID
        }

        override fun areContentsTheSame(oldItem: ItemMeta, newItem: ItemMeta): Boolean {
            return oldItem.firestoreID == newItem.firestoreID
                    && oldItem.name == newItem.name
                    && oldItem.sharedWith.contentEquals(newItem.sharedWith)
                    && oldItem.quantity == newItem.quantity
                    && oldItem.timeStamp == newItem.timeStamp
        }
    }

    inner class VH(private val rowBinding: RowItemBinding) :
        RecyclerView.ViewHolder(rowBinding.root) {

        fun bind(holder: VH, position: Int) {
//            val itemMeta = viewModel.getItemMeta(position)
            val itemMeta = ItemMeta()
            holder.rowBinding.itemName.text = itemMeta.name
            holder.rowBinding.itemQuantity.text = itemMeta.quantity.toString()
            rowBinding.root.setOnClickListener {
                val action = ListFragmentDirections.actionListFragmentToCompleteExpense(itemMeta.firestoreID)
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