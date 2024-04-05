package edu.utap.recyclerviewfrag

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import edu.utap.recyclerviewfrag.databinding.RowBinding

/**
 * Created by witchel on 8/25/2019
 */

class SelectAdapter(private val viewModel: MainViewModel)
    : RecyclerView.Adapter<SelectAdapter.VH>() {
    companion object {
        val TAG = "SelectAdapter"
    }

    // https://developer.android.com/reference/androidx/recyclerview/widget/RecyclerView.ViewHolder#getBindingAdapterPosition()
    // Getting the position of the selected item is unfortunately complicated
    // This always returns a valid index.
    private fun getPos(holder: VH) : Int {
        val pos = holder.bindingAdapterPosition
        // notifyDataSetChanged was called, so position is not known
        if( pos == RecyclerView.NO_POSITION) {
            return holder.absoluteAdapterPosition
        }
        return pos
    }
    // ViewHolder pattern
    inner class VH(val rowBinding: RowBinding)
        : RecyclerView.ViewHolder(rowBinding.root) {
        init {
//            rowBinding.rowText.setOnClickListener {
//                val previous = viewModel.selected
//                val position = getPos(this)
//                // Manipulate ViewModel here because this call back comes directly from
//                // View (though ViewHolder is an inner class)
//                viewModel.selected = position
//                if (previous >= 0)
//                    notifyItemChanged(previous)
//                notifyItemChanged(position)
//                val context = it.context
//                val item = viewModel.getItemAt(position)
//                item.let {
//                    val selected = "You selected $position ${it.name}"
//                    Toast.makeText(context, selected, Toast.LENGTH_SHORT).show()
//                }
//            }
//            rowBinding.rowFav.setOnClickListener {
//                val position = getPos(this)
//                // Toggle Favorite
//                val local = viewModel.getItemAt(position)
//                local.let {
//                    if (viewModel.isFavorite(it)) {
//                        viewModel.removeFavorite(it)
//                    } else {
//                        viewModel.addFavorite(it)
//                    }
//                    notifyItemChanged(position)
//                }
//            }
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
//        if (viewModel.isFavorite(item)) {
//            binding.rowFav.setImageResource(R.drawable.ic_favorite_black_24dp)
//        } else {
//            binding.rowFav.setImageResource(R.drawable.ic_favorite_border_black_24dp)
//        }
//        if (viewModel.selected == position) {
//            binding.rowCheck.setImageResource(R.drawable.ic_check_black_24dp)
//        } else {
//            binding.rowCheck.setImageDrawable(ColorDrawable(Color.WHITE))
//        }
    }

    override fun getItemCount() = viewModel.getItemCount()
}

