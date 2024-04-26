package com.example.sharedhouse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sharedhouse.databinding.FeedViewBinding

class FeedViewFragment : Fragment() {
    companion object {
        val TAG : String = "SelectFragment"
    }
    // https://developer.android.com/topic/libraries/view-binding#fragments
    private var _binding: FeedViewBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
//    private val viewModel: MainViewModel by activityViewModels()

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
//        binding.recyclerView.layoutManager = LinearLayoutManager(binding.recyclerView.context)
//        binding.recyclerView.adapter = SelectAdapter(viewModel)
        binding.swipeRefreshLayout.setOnRefreshListener {
            //TODO: add call to VM
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}