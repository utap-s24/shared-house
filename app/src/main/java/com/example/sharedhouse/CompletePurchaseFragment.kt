package com.example.sharedhouse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sharedhouse.databinding.AddExpenseBinding
import com.example.sharedhouse.databinding.CompletePurchaseBinding

class CompletePurchaseFragment : Fragment() {
    // https://developer.android.com/topic/libraries/view-binding#fragments
    private var _binding: CompletePurchaseBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CompletePurchaseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.submitButton.setOnClickListener {
            if (binding.priceTextField.text.isNotEmpty()) {
                //Valid
                var amount = binding.priceTextField.text.toString().toDouble()
                if (binding.taxCheckbox.isChecked) {
                    amount *= 106.25
                }
                var comments = mutableListOf<String>()
                if (binding.commentsTextField.text.isNotEmpty()) {
                    comments.add(binding.commentsTextField.text.toString())
                }

                //TODO: Connect to VM
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