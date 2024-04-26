package com.example.sharedhouse

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedhouse.databinding.RowApartmentBinding
import com.example.sharedhouse.databinding.RowItemBinding
import com.example.sharedhouse.db.MainViewModel
import com.example.sharedhouse.models.Apartment

class ApartmentListAdapter(private val viewModel: MainViewModel,
                           private val clickListener: (index : String)->Unit): ListAdapter<Apartment,
        ApartmentListAdapter.ViewHolder>(Diff()) {
    class Diff : DiffUtil.ItemCallback<Apartment>() {
        // Item identity
        override fun areItemsTheSame(oldItem: Apartment, newItem: Apartment): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
        // Item contents are the same, but the object might have changed
        override fun areContentsTheSame(oldItem: Apartment, newItem: Apartment): Boolean {
            return oldItem.firestoreID == newItem.firestoreID
                    && oldItem.name == newItem.name
        }
    }

    inner class ViewHolder(private val rowBinding: RowApartmentBinding) :
        RecyclerView.ViewHolder(rowBinding.root) {

        fun bind(holder: ViewHolder, position: Int) {
            val apartment = viewModel.getApartmentMeta(position)
            holder.rowBinding.apartmentName.text = apartment.name
            rowBinding.joinButton.setOnClickListener {
                clickListener(apartment.firestoreID)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //XXX Write me.
        val rowBinding = RowApartmentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)
        return ViewHolder(rowBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //XXX Write me.
        holder.bind(holder, position)
    }
}