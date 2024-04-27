package com.example.sharedhouse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.sharedhouse.databinding.CompletePurchaseBinding
import com.example.sharedhouse.db.MainViewModel


//User is about to complete a purchase of an item.
class CompletePurchaseFragment : Fragment() {
    // https://developer.android.com/topic/libraries/view-binding#fragments
    private var _binding: CompletePurchaseBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    private val args: CompletePurchaseFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CompletePurchaseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val index = args.index
        val currentUnpurchasedExpense = viewModel.getItemMeta(index)

        binding.titleTextView.text = "Complete purchase for: ${currentUnpurchasedExpense.itemName}"
        binding.priceTextField.hint = "Price for ${currentUnpurchasedExpense.itemName}"

        viewModel.observeAllRoomates().observe(viewLifecycleOwner) {
            var names = ""
            for (sharedId in currentUnpurchasedExpense.sharedWith) {
                if (it.containsKey(sharedId)) {
                    names += "${it[sharedId]}, "

                }
            }
            names = names.substring(0, names.length - 2)
            binding.sharedWithTextView.text = names
        }

        binding.submitButton.setOnClickListener {
            if (binding.priceTextField.text.isNotEmpty()) {
                //Valid
                var amount = binding.priceTextField.text.toString().toDouble()
                if (binding.taxCheckbox.isChecked) {
                    amount += (amount * .0625)
                }
                var comments : String = ""
                if (binding.commentsTextField.text.isNotEmpty()) {
                    comments = binding.commentsTextField.text.toString()
                }

                viewModel.addPurchasedItem(currentUnpurchasedExpense, amount, comments)
                findNavController().popBackStack()
            } else {
                binding.priceTextField.error = "Please enter a price."
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}