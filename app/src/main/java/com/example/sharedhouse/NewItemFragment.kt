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
import com.example.sharedhouse.databinding.NewItemBinding
import com.example.sharedhouse.db.MainViewModel
import com.example.sharedhouse.models.UnpurchasedExpense

class NewItemFragment : Fragment() {
    // https://developer.android.com/topic/libraries/view-binding#fragments
    private var _binding: NewItemBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private val sharedWith = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NewItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var roommateCount = 0
        viewModel.getAllRoomates()
        viewModel.observeCurrentApartment().observe(viewLifecycleOwner) {
            roommateCount = it.roomates.size
            var apt = it



            when (roommateCount) {
                0 -> {
                    binding.layoutOne.visibility = View.INVISIBLE
                    binding.layoutTwo.visibility = View.INVISIBLE
                    binding.layoutThree.visibility = View.INVISIBLE
                    binding.layoutFour.visibility = View.INVISIBLE
                    binding.layoutOne.isEnabled = false
                    binding.layoutTwo.isEnabled = false
                    binding.layoutThree.isEnabled = false
                    binding.layoutFour.isEnabled = false

                }

                1 -> {
                    binding.layoutOne.visibility = View.VISIBLE
                    binding.layoutTwo.visibility = View.INVISIBLE
                    binding.layoutThree.visibility = View.INVISIBLE
                    binding.layoutFour.visibility = View.INVISIBLE
                    binding.layoutOne.isEnabled = true
                    binding.layoutTwo.isEnabled = false
                    binding.layoutThree.isEnabled = false
                    binding.layoutFour.isEnabled = false
                    sharedWith.add(it.roomates[0])
                    viewModel.observeAllRoomates().observe(viewLifecycleOwner) {
                        binding.labelTextView1.text = it[apt.roomates[0]]
                    }
                }

                2 -> {
                    binding.layoutOne.visibility = View.VISIBLE
                    binding.layoutTwo.visibility = View.VISIBLE
                    binding.layoutThree.visibility = View.INVISIBLE
                    binding.layoutFour.visibility = View.INVISIBLE
                    binding.layoutOne.isEnabled = true
                    binding.layoutTwo.isEnabled = true
                    binding.layoutThree.isEnabled = false
                    binding.layoutFour.isEnabled = false
                    sharedWith.add(it.roomates[0])
                    sharedWith.add(it.roomates[1])
                    viewModel.observeAllRoomates().observe(viewLifecycleOwner) {
                        binding.labelTextView1.text = it[apt.roomates[0]]
                        binding.labelTextView2.text = it[apt.roomates[1]]
                    }

                }

                3 -> {
                    binding.layoutOne.visibility = View.VISIBLE
                    binding.layoutTwo.visibility = View.VISIBLE
                    binding.layoutThree.visibility = View.VISIBLE
                    binding.layoutFour.visibility = View.INVISIBLE
                    binding.layoutOne.isEnabled = true
                    binding.layoutTwo.isEnabled = true
                    binding.layoutThree.isEnabled = true
                    binding.layoutFour.isEnabled = false
                    sharedWith.add(it.roomates[0])
                    sharedWith.add(it.roomates[1])
                    sharedWith.add(it.roomates[2])
                    viewModel.observeAllRoomates().observe(viewLifecycleOwner) {
                        binding.labelTextView1.text = it[apt.roomates[0]]
                        binding.labelTextView2.text = it[apt.roomates[1]]
                        binding.labelTextView3.text = it[apt.roomates[2]]

                    }
                }

                4 -> {
                    binding.layoutOne.visibility = View.VISIBLE
                    binding.layoutTwo.visibility = View.VISIBLE
                    binding.layoutThree.visibility = View.VISIBLE
                    binding.layoutFour.visibility = View.VISIBLE
                    binding.layoutOne.isEnabled = true
                    binding.layoutTwo.isEnabled = true
                    binding.layoutThree.isEnabled = true
                    binding.layoutFour.isEnabled = true
                    sharedWith.add(it.roomates[0])
                    sharedWith.add(it.roomates[1])
                    sharedWith.add(it.roomates[2])
                    sharedWith.add(it.roomates[3])
                    viewModel.observeAllRoomates().observe(viewLifecycleOwner) {
                        binding.labelTextView1.text = it[apt.roomates[0]]
                        binding.labelTextView2.text = it[apt.roomates[1]]
                        binding.labelTextView3.text = it[apt.roomates[2]]
                        binding.labelTextView4.text = it[apt.roomates[3]]

                    }

                }

                else -> println("Error")
            }

            binding.submitButton.setOnClickListener {
                if (binding.itemTextField.text.isNotEmpty() && binding.quantityTextField.text.isNotEmpty()) {
                    if (binding.meCheckbox.isChecked || binding.roommateCheckboxOne.isChecked
                        || binding.roommateCheckboxTwo.isChecked || binding.roommateCheckboxThree.isChecked
                        || binding.roommateCheckboxFour.isChecked
                    ) {
                        // Valid setup of item details.
                        //TODO: Call viewmodel function here
                        viewModel.addUnpurchasedExpense(
                            UnpurchasedExpense(
                                binding.itemTextField.text.toString(),
                                sharedWith,
                                binding.quantityTextField.text.toString().toInt(),
                            )
                        )
                    }
                }
            }
        }

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}