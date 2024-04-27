package com.example.sharedhouse

import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginLeft
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedhouse.databinding.RowCommentBinding
import com.example.sharedhouse.databinding.RowCommentOtherBinding
import com.google.firebase.auth.FirebaseAuth

class CompletedExpenseViewAdapter(private val comments: List<HashMap<String,String>>)
    : ListAdapter<HashMap<String,String >, RecyclerView.ViewHolder>(Diff()) {
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

    inner class RowCommentOtherBindingVH(private val rowBinding: RowCommentOtherBinding ) :
        RecyclerView.ViewHolder(rowBinding.root) {

        fun bind(holder: RowCommentOtherBindingVH, position: Int) {
            val itemMeta = comments[position]
            holder.rowBinding.comment.text = itemMeta["comment"]
            holder.rowBinding.commenter.text = itemMeta["name"]
        }
    }

    inner class RowCommentBindingVH(private val rowBinding: RowCommentBinding ) :
        RecyclerView.ViewHolder(rowBinding.root) {

        fun bind(holder: RowCommentBindingVH, position: Int) {
            val itemMeta = comments[position]
            holder.rowBinding.comment.text = itemMeta["comment"]
            holder.rowBinding.commenter.text = itemMeta["name"]

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            1 -> {
                val rowBinding = RowCommentOtherBinding.inflate(LayoutInflater.from(parent.context),
                    parent, false)
                return RowCommentOtherBindingVH(rowBinding)
            }
            else -> {
                val rowBinding = RowCommentBinding.inflate(LayoutInflater.from(parent.context),
                    parent, false)
                return RowCommentBindingVH(rowBinding)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType) {
            1 -> {
                (holder as RowCommentOtherBindingVH).bind(holder, position)
            }
            else -> {
                (holder as RowCommentBindingVH).bind(holder, position)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val itemMeta = comments[position]
        if (itemMeta["name"] == FirebaseAuth.getInstance().currentUser!!.displayName) {
            return 1
        } else {
            return 2
        }
    }
}