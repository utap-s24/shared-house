package com.example.sharedhouse

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedhouse.databinding.ApartmentViewBinding
import com.example.sharedhouse.databinding.FeedViewBinding
import com.example.sharedhouse.databinding.ProfileViewBinding
import com.example.sharedhouse.db.MainViewModel
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract

class ApartmentListFragment : Fragment() {
    companion object {
        val TAG : String = ApartmentListFragment::class.java.simpleName
    }

    private var _binding: ApartmentViewBinding? = null
    private val binding get() = _binding!!
    private val dataViewModel: MainViewModel by activityViewModels()
    private lateinit var adapter: ApartmentListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ApartmentViewBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val joinClickListener: (index: Int) -> Unit = { index ->
            //Visual elements
            dataViewModel.addUserToExistingApartment(dataViewModel.getAllApartments()[index].apartmentID)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ApartmentListAdapter(dataViewModel, joinClickListener)
//        adapter.submitList(dataViewModel.getAllApartments())
        binding.recyclerView.adapter = adapter
        initRecyclerViewDividers(binding.recyclerView)
        binding.buttonCreate.setOnClickListener {
            dataViewModel.addNewApartment(binding.newApartmentName.text.toString())
        }
    }

    private fun initRecyclerViewDividers(rv: RecyclerView) {
        // Let's have dividers between list items
        val dividerItemDecoration = DividerItemDecoration(
            rv.context, LinearLayoutManager.VERTICAL
        )
        rv.addItemDecoration(dividerItemDecoration)
    }
}