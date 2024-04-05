package edu.utap.recyclerviewfrag

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.utap.recyclerviewfrag.databinding.RowBinding

/**
 * Created by witchel on 8/25/2019
 */

class FavoritesAdapter(private val viewModel: MainViewModel)
    : RecyclerView.Adapter<FavoritesAdapter.VH>() {
    companion object {
        val TAG = "FavoritesAdapter"
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
//            rowBinding.rowFav.setOnClickListener {
//                val position = getPos(this)
//                // Toggle Favorite
//                val local = viewModel.getFavoriteAt(position)
//                local.let {
//                    if (viewModel.isFavorite(it)) {
//                        viewModel.removeFavorite(it)
//                        notifyItemRemoved(position)
//                    } else {
//                        viewModel.addFavorite(it)
//                        notifyItemChanged(position)
//                    }
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
//        val adapterPosition = getPos(holder)
//        val item = viewModel.getFavoriteAt(adapterPosition)
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
    }

    override fun getItemCount() = viewModel.getFavoriteCount()
}

