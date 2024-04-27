package com.example.sharedhouse

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.sharedhouse.databinding.AddExpenseBinding
import com.example.sharedhouse.databinding.CompletePurchaseBinding
import com.example.sharedhouse.db.MainViewModel

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

        //TODO: get data based on firebaseID

        binding.titleTextView.text = "Complete purchase for: ${currentUnpurchasedExpense.itemName}"
        binding.priceTextField.hint = "Price for ${currentUnpurchasedExpense.itemName}"
        viewModel.observeAllRoomates().observe(viewLifecycleOwner) {
            Log.d("CompletedExpenseView", "the map from id to name : $it")
            var names = ""
            for (sharedId in currentUnpurchasedExpense.sharedWith) {
                if (it.containsKey(sharedId)) {
                    names += "${it[sharedId]}, "

                }
            }
            Log.d("CompletedExpenseView", "names: ${names}")
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
                Log.d("FeedViewFragment", "we're getting here")

                //TODO: Connect to VM - firebase id val available above
                viewModel.addPurchasedItem(currentUnpurchasedExpense, amount, comments)
            }
        }


//        binding.pictureButton.setOnClickListener {
//
//        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}