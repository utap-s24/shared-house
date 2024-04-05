package edu.utap.recyclerviewfrag

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import edu.utap.recyclerviewfrag.databinding.FeedViewBinding

class SelectFragment : Fragment() {
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
        val root: View = binding.root
        Log.d(SimpleFragment.TAG, "onCreateView ${viewModel.selected}")
        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.recyclerView.layoutManager = LinearLayoutManager(binding.recyclerView.context)
//        binding.recyclerView.adapter = SelectAdapter(viewModel)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}