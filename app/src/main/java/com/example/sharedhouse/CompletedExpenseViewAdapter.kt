package com.example.sharedhouse

import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedhouse.databinding.RowCommentBinding
import com.google.firebase.auth.FirebaseAuth

class CompletedExpenseViewAdapter(private val comments: List<HashMap<String,String>>)
    : ListAdapter<HashMap<String,String >, CompletedExpenseViewAdapter.VH>(Diff()) {
    // This class allows the adapter to compute what has changed
    class Diff : DiffUtil.ItemCallback<HashMap<String,String >>() {
        override fun areItemsTheSame(oldItem: HashMap<String,String >, newItem: HashMap<String,String >): Boolean {
            return oldItem["comment"] == newItem["comment"] && oldItem["name"] == newItem["name"]
        }

        override fun areContentsTheSame(oldItem: HashMap<String, String>, newItem: HashMap<String, String>): Boolean {
            // With only one key-value pair, the contents are the same if the entries are the same
            return areItemsTheSame(oldItem, newItem)
        }
    }

    inner class VH(private val rowBinding: RowCommentBinding) :
        RecyclerView.ViewHolder(rowBinding.root) {

        fun bind(holder: VH, position: Int) {
            val itemMeta = comments[position]
            holder.rowBinding.comment.text = itemMeta["comment"]
            holder.rowBinding.commenter.text = itemMeta["name"]
            //Change how the comment renders depending on who wrote it
            if (itemMeta["name"] == FirebaseAuth.getInstance().currentUser!!.displayName) {
                holder.rowBinding.comment.textAlignment = View.TEXT_ALIGNMENT_VIEW_END
                holder.rowBinding.commenterLayout.gravity = Gravity.RIGHT
            } else {
                holder.rowBinding.comment.textAlignment = View.TEXT_ALIGNMENT_VIEW_START
                holder.rowBinding.commenterLayout.gravity = Gravity.LEFT
            }
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