package edu.utap.recyclerviewfrag

import androidx.lifecycle.ViewModel

class  MainViewModel : ViewModel() {
    private var repository = Repository()
    // Maintain a list of all Data items
    private var list = repository.fetchData()

    // Maintain a separate list of all favorite Data items
    private var favAlbums = mutableListOf<Data>()
    internal var selected = -1

    fun getItemAt(position: Int) : Data {
        return list[position]
    }
    fun getFavoriteAt(position: Int) : Data {
        return favAlbums[position]
    }

    fun isFavorite(albumRec: Data): Boolean {
        return favAlbums.contains(albumRec)
    }
    fun addFavorite(albumRec: Data) {
        favAlbums.add(albumRec)
    }
    fun removeFavorite(albumRec: Data) {
        favAlbums.remove(albumRec)
    }

    fun getItemCount() : Int {
        return list.size
    }
    fun getFavoriteCount() : Int {
        return favAlbums.size
    }
}