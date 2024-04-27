package com.example.sharedhouse

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedhouse.databinding.CompletedExpenseViewBinding
import com.example.sharedhouse.databinding.ItemsListBinding
import com.example.sharedhouse.db.MainViewModel
import com.google.firebase.auth.FirebaseAuth

class CompletedExpenseViewFragment : Fragment() {
    private var _binding: CompletedExpenseViewBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private val authViewModel: AuthViewModel by activityViewModels()

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

        val index = args.index
        var currentPurchasedExpense = viewModel.getPurchasedItemMeta(index)


        binding.priceText.text = String.format("%.2f", currentPurchasedExpense.price)

        if (currentPurchasedExpense.quantity == 0) {
            binding.quantityLayout.visibility = View.GONE
            binding.quantityText.text = "should not see this"
            binding.titleTextView.text = "Expense: ${currentPurchasedExpense.name}"

        } else {
            binding.quantityLayout.visibility = View.VISIBLE
            binding.quantityText.text = currentPurchasedExpense.quantity.toString()
            binding.titleTextView.text = "Purchase: ${currentPurchasedExpense.name}"
        }


        viewModel.observeAllRoomates().observe(viewLifecycleOwner) {
            Log.d("CompletedExpenseView", "the map from id to name : $it")
            var names = ""
            for (sharedId in currentPurchasedExpense.hasPaid.keys) {
              if (it.containsKey(sharedId)) {
                names += "${it[sharedId]}, "

              }
            }
            Log.d("CompletedExpenseView", "names: ${names}")
            names = names.substring(0, names.length - 2)
            binding.sharedWithTextView.text = names
            binding.purchaserText.text = it[currentPurchasedExpense.purchasedBy]

            if (currentPurchasedExpense.hasPaid.containsKey(FirebaseAuth.getInstance().currentUser!!.uid)) {
                binding.paidLayout.visibility = View.VISIBLE
                if (currentPurchasedExpense.hasPaid[FirebaseAuth.getInstance().currentUser!!.uid] == true) {
                    binding.payButton.visibility = View.GONE
                    binding.payButton.isClickable = false
                    binding.paidText.text = "Paid"
                } else {
                    binding.payButton.visibility = View.VISIBLE
                    binding.paidText.text = "Not Paid"
                    binding.paidLayout.isClickable = true
                    binding.payButton.setOnClickListener {

                        viewModel.updateHasPaid(currentPurchasedExpense)
                        binding.paidText.text = "Paid"
                        binding.payButton.isClickable = false
                        binding.payButton.visibility = View.GONE
                    }
                }
            } else {
                binding.paidLayout.visibility = View.GONE
                binding.payButton.isClickable = false
            }
        }

        binding.addComment.setOnClickListener {
            if (binding.commentsTextField.text.isNotEmpty()) {
                //We have a comment to add
                viewModel.addCommentToPurchasedItem(currentPurchasedExpense, binding.commentsTextField.text.toString())
                binding.commentsTextField.text.clear()
            } else {
                binding.commentsTextField.error = "Please enter a comment."
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.updatePurchasedItems()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        val adapter = CompletedExpenseViewAdapter(currentPurchasedExpense.comments)
        val rv = binding.recyclerView
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(rv.context, LinearLayoutManager.VERTICAL, false)
        adapter.submitList(currentPurchasedExpense.comments)

        viewModel.observePurchasedItems().observe(viewLifecycleOwner) {
            val newAdapter =  CompletedExpenseViewAdapter(it[index].comments)
            binding.recyclerView.adapter = newAdapter
            newAdapter.submitList(it[index].comments)
            Log.d("CompletedExpenseView", "submitting list ${currentPurchasedExpense.comments}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


