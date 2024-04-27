package com.example.sharedhouse

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sharedhouse.databinding.FeedViewBinding
import com.example.sharedhouse.db.MainViewModel
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sharedhouse.models.PurchasedItem

class FeedViewFragment : Fragment() {
    companion object {
        val TAG : String = "SelectFragment"
    }
    // https://developer.android.com/topic/libraries/view-binding#fragments
    private var _binding: FeedViewBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FeedViewBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = FeedViewAdapter(viewModel, view.findNavController())
        val rv = binding.recyclerView
        val itemDecor = DividerItemDecoration(rv.context, LinearLayoutManager.VERTICAL)
        rv.addItemDecoration(itemDecor)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(rv.context)

        binding.swipeRefreshLayout.setOnRefreshListener {
            //TODO: add call to VM
            viewModel.updatePurchasedItems()
            binding.swipeRefreshLayout.isRefreshing = false
        }
        viewModel.observeCurrentApartment().observe(viewLifecycleOwner) {
            Log.d("FeedViewFragment", "Current apartment: $it")
            viewModel.updatePurchasedItems()

        }

        viewModel.observePurchasedItems().observe(viewLifecycleOwner) {
            Log.d("FeedViewFragment", "Purchased items observed: $it")
            val list = it
            adapter.submitList(it)
            viewModel.observeAllRoomates().observe(viewLifecycleOwner) {
                adapter.submitList(list)
            }
        }




    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}