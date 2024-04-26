package com.example.sharedhouse

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sharedhouse.databinding.CompletedExpenseViewBinding
import com.example.sharedhouse.databinding.ItemsListBinding
import com.example.sharedhouse.db.MainViewModel

class CompletedExpenseViewFragment : Fragment() {
    private var _binding: CompletedExpenseViewBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    private val args: CompletedExpenseViewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CompletedExpenseViewBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val firebaseId = args.id

        //TODO: Get all the data corresponding to this expense from firebaseId

        binding.addComment.setOnClickListener {
            if (binding.commentsTextField.text.isNotEmpty()) {
                //We have a comment to add
                //TODO: add comment
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            //TODO: call viewmodel method to refresh
        }

        val adapter = ItemListAdapter(viewModel, findNavController())
        val rv = binding.recyclerView
        val itemDecor = DividerItemDecoration(rv.context, LinearLayoutManager.VERTICAL)
        rv.addItemDecoration(itemDecor)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(rv.context)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
