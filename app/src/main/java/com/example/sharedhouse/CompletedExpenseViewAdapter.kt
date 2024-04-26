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

class CompletedExpenseViewAdapter(private val viewModel: MainViewModel, private val navController: NavController, private val comments: List<HashMap<String,String>>)
    : ListAdapter<HashMap<String,String >, CompletedExpenseViewAdapter.VH>(Diff()) {
    // This class allows the adapter to compute what has changed
    class Diff : DiffUtil.ItemCallback<HashMap<String,String >>() {
        override fun areItemsTheSame(oldItem: HashMap<String,String >, newItem: HashMap<String,String >): Boolean {
            val oldEntry = oldItem.entries.firstOrNull()
            val newEntry = newItem.entries.firstOrNull()
            return oldEntry?.key == newEntry?.key && oldEntry?.value == newEntry?.value
        }

        override fun areContentsTheSame(oldItem: HashMap<String, String>, newItem: HashMap<String, String>): Boolean {
            // With only one key-value pair, the contents are the same if the entries are the same
            return areItemsTheSame(oldItem, newItem)
        }
    }

    inner class VH(private val rowBinding: RowCommentBinding) :
        RecyclerView.ViewHolder(rowBinding.root) {

        fun bind(holder: VH, position: Int) {
            //TODO: link up viewModel to get the specific item at position
            val itemMeta = comments[position]
            //TODO: attach comment and name to view binding.
            holder.rowBinding.itemName.text = itemMeta["name"]
//            holder.rowBinding.commenter.text =
//             if the id of current user == id of the commenter:
//              holder.rowBinding.layout.setHorizontalGravity(Gravity.RIGHT)
//            else {
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