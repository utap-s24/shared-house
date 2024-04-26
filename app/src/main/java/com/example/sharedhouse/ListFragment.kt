package com.example.sharedhouse

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sharedhouse.databinding.ItemsListBinding
import com.example.sharedhouse.db.MainViewModel

class ListFragment : Fragment() {
    // https://developer.android.com/topic/libraries/view-binding#fragments
    private var _binding: ItemsListBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ItemsListBinding.inflate(inflater, container, false)
        val root: View = binding.root
//        Log.d("FavoritesFragment", "onCreateView ${viewModel.selected}")
        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addItem.setOnClickListener {
            findNavController().navigate(R.id.newItemFragment)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            //TODO: add call to VM
            viewModel.updateUnpurchasedItems()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        val adapter = ItemListAdapter(viewModel, findNavController())
        val rv = binding.recyclerView
        val itemDecor = DividerItemDecoration(rv.context, LinearLayoutManager.VERTICAL)
        rv.addItemDecoration(itemDecor)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(rv.context)
        viewModel.observeCurrentApartment().observe(viewLifecycleOwner) {
            Log.d("ListFragment", "Current apartment: $it")
            viewModel.updateUnpurchasedItems()
        }
        viewModel.observeUnpurchasedItems().observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        binding.addItem.setOnClickListener {
            findNavController().navigate(R.id.newItemFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
