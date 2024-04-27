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
import androidx.fragment.app.viewModels
import com.example.sharedhouse.databinding.AddExpenseBinding
import com.example.sharedhouse.db.MainViewModel
import com.example.sharedhouse.models.PurchasedItem
import com.example.sharedhouse.models.UnpurchasedExpense
import com.google.firebase.auth.FirebaseAuth

class AddExpenseFragment : Fragment() {
    // https://developer.android.com/topic/libraries/view-binding#fragments
    private var _binding: AddExpenseBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
    private val sharedWith = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var roommateCount = 0
        viewModel.observeCurrentApartment().observe(viewLifecycleOwner) {
            viewModel.getAllRoomates()
            roommateCount = it.roomates.size
            var apt = it



            when (roommateCount) {
                0 -> {
                    binding.layoutOne.visibility = View.GONE
                    binding.layoutTwo.visibility = View.GONE
                    binding.layoutThree.visibility = View.GONE
                    binding.layoutFour.visibility = View.GONE
                    binding.layoutOne.isEnabled = false
                    binding.layoutTwo.isEnabled = false
                    binding.layoutThree.isEnabled = false
                    binding.layoutFour.isEnabled = false
                    binding.roommateCheckboxOne.isChecked = false
                    binding.roommateCheckboxTwo.isChecked = false
                    binding.roommateCheckboxThree.isChecked = false
                    binding.roommateCheckboxFour.isChecked = false
                }

                1 -> {
                    binding.layoutOne.visibility = View.VISIBLE
                    binding.layoutTwo.visibility = View.GONE
                    binding.layoutThree.visibility = View.GONE
                    binding.layoutFour.visibility = View.GONE
                    binding.layoutOne.isEnabled = true
                    binding.layoutTwo.isEnabled = false
                    binding.layoutThree.isEnabled = false
                    binding.layoutFour.isEnabled = false
                    binding.roommateCheckboxTwo.isChecked = false
                    binding.roommateCheckboxThree.isChecked = false
                    binding.roommateCheckboxFour.isChecked = false
                    sharedWith.add(it.roomates[0])
                    viewModel.observeAllRoomates().observe(viewLifecycleOwner) {
                        binding.labelTextView1.text = it[apt.roomates[0]]
                    }
                }

                2 -> {
                    binding.layoutOne.visibility = View.VISIBLE
                    binding.layoutTwo.visibility = View.VISIBLE
                    binding.layoutThree.visibility = View.GONE
                    binding.layoutFour.visibility = View.GONE
                    binding.layoutOne.isEnabled = true
                    binding.layoutTwo.isEnabled = true
                    binding.layoutThree.isEnabled = false
                    binding.layoutFour.isEnabled = false
                    binding.roommateCheckboxThree.isChecked = false
                    binding.roommateCheckboxFour.isChecked = false
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
                    binding.layoutFour.visibility = View.GONE
                    binding.layoutOne.isEnabled = true
                    binding.layoutTwo.isEnabled = true
                    binding.layoutThree.isEnabled = true
                    binding.layoutFour.isEnabled = false
                    binding.roommateCheckboxFour.isChecked = false

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
                if (binding.expenseText.text.isNotEmpty() && binding.amountText.text.isNotEmpty()) {
                    if (binding.meCheckbox.isChecked || binding.roommateCheckboxOne.isChecked
                        || binding.roommateCheckboxTwo.isChecked || binding.roommateCheckboxThree.isChecked
                        || binding.roommateCheckboxFour.isChecked
                    ) {
                        val title = binding.expenseText.text
                        var amount = binding.amountText.text.toString().toDouble()
                        if (binding.taxCheckbox.isChecked) {
                            amount *= 1.0625
                        }


                        val map = HashMap<String, Boolean>()
                        for (id in sharedWith){
                            map[id] = false
                        }

                        if (binding.meCheckbox.isChecked){
                            map[FirebaseAuth.getInstance().currentUser!!.uid] = true
                        }

                        val expenseToAdd = PurchasedItem(
                            purchasedBy = FirebaseAuth.getInstance().currentUser!!.uid,
                            name = title.toString(),
                            price = amount,
                            hasPaid = map,
                            quantity = 0
                        )

                        viewModel.addPurchasedExpense(expenseToAdd)

                    }
                }
            }
        }

        binding.submitButton.setOnClickListener {
            if (binding.expenseText.text.isNotEmpty() && binding.amountText.text.isNotEmpty()) {
                val title = binding.expenseText.text
                var amount = binding.amountText.text.toString().toDouble()
                if (binding.taxCheckbox.isChecked) {
                    amount *= 106.25
                }

                //TODO: Call to viewmodel
            }
        }



    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}