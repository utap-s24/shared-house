package edu.utap.recyclerviewfrag

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import edu.utap.recyclerviewfrag.databinding.RowBinding

/**
 * Created by witchel on 8/25/2019
 */

class SimpleAdapter(private val viewModel: MainViewModel)
    : RecyclerView.Adapter<SimpleAdapter.VH>() {
    companion object {
        val TAG = "SimpleAdapter"
    }
    // https://developer.android.com/reference/androidx/recyclerview/widget/RecyclerView.ViewHolder#getBindingAdapterPosition()
    // Getting the position of the selected item is unfortunately complicated
    // This always returns a valid index.
    private fun getPos(holder: RecyclerView.ViewHolder) : Int {
        val pos = holder.bindingAdapterPosition
        // notifyDataSetChanged was called, so position is not known
        if( pos == RecyclerView.NO_POSITION) {
            return holder.absoluteAdapterPosition
        }
        return pos
    }
    override fun getItemCount(): Int {
        return viewModel.getItemCount()
    }
    // ViewHolder pattern
    inner class VH(val rowBinding: RowBinding)
        : RecyclerView.ViewHolder(rowBinding.root) {
        init {
            rowBinding.root.setOnClickListener {
                val previous = viewModel.selected
                val position = getPos(this)
                // Manipulate ViewModel here because this call back comes directly from
                // View (though ViewHolder is an inner class)
                viewModel.selected = position
                if (previous >= 0)
                    notifyItemChanged(previous)
                notifyItemChanged(position)
                val context = it.context
                val item = viewModel.getItemAt(position)
                item.let {
                    val selected = "You selected $position ${it.name}"
                    Toast.makeText(context, selected, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val rowBinding = RowBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return VH(rowBinding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
//        val item = viewModel.getItemAt(position)
//        val binding = holder.rowBinding
//        binding.rowText.text = item.name
//        if (item.rating) {
//            binding.rowPic.setImageResource(R.drawable.emoticon_excited)
//        } else {
//            binding.rowPic.setImageResource(R.drawable.emoticon_dead)
//        }
        // Simple only shows text and image
    }
}

