package com.example.sharedhouse

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedhouse.databinding.RowApartmentBinding
import com.example.sharedhouse.db.MainViewModel

class ApartmentListAdapter(private val viewModel: MainViewModel,
                           private val clickListener: (songIndex : Int)->Unit): ListAdapter<Apartment,
        RVDiffAdapter.ViewHolder>(Diff()) {
    class Diff : DiffUtil.ItemCallback<Apartment>() {
        // Item identity
        override fun areItemsTheSame(oldItem: Apartment, newItem: Apartment): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
        // Item contents are the same, but the object might have changed
        override fun areContentsTheSame(oldItem: Apartment, newItem: Apartment): Boolean {
            return oldItem.name == newItem.name
                    && oldItem.rawId == newItem.rawId
                    && oldItem.time == newItem.time
        }
    }

    private fun getPos(holder: ViewHolder) : Int {
        val pos = holder.adapterPosition
        // notifyDataSetChanged was called, so position is not known
        if( pos == RecyclerView.NO_POSITION) {
            return holder.layoutPosition
        }
        return pos
    }

    inner class ViewHolder(val rowApartmentBinding: RowApartmentBinding)
        : RecyclerView.ViewHolder(rowApartmentBinding.root) {
        init {
            //XXX Write me.
            rowApartmentBinding.root.setOnClickListener {
                clickListener(getPos(this))
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
        val apartmentList = viewModel.getApartmentList()
        val apartment = apartmentList[position]
        holder.rowApartmentBinding.apartmentName = apartment.
        holder.songRowBinding.textTime.text = song.time
        if (position == viewModel.currentIndex) {
            MainActivity.setBackgroundColor(holder.itemView, Color.YELLOW)
        } else {
            MainActivity.setBackgroundColor(holder.itemView, Color.TRANSPARENT)
        }
    }
}