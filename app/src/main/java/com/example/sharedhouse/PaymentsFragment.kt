package com.example.sharedhouse

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sharedhouse.databinding.PaymentsFragmentBinding
import com.example.sharedhouse.db.MainViewModel

class PaymentsFragment : Fragment() {
    companion object {
        val TAG : String = PaymentsFragment::class.java.simpleName
    }
    // https://developer.android.com/topic/libraries/view-binding#fragments
    private var _binding: PaymentsFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val dataViewModel: MainViewModel by activityViewModels()
    private val sharedWith = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PaymentsFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root
//        Log.d(TAG, "onCreateView ${viewModel.selected}")
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //EEE // XXX Write me, action bar title,
        binding.addExpense.setOnClickListener {
            findNavController().navigate(R.id.addExpenseFragment)
        }

        var roommateCount = 0
        dataViewModel.getAllRoomates()
        dataViewModel.observeCurrentApartment().observe(viewLifecycleOwner) {
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
                    dataViewModel.observeAllRoomates().observe(viewLifecycleOwner) {
                        binding.personOne.text = it[apt.roomates[0]]
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
                    dataViewModel.observeAllRoomates().observe(viewLifecycleOwner) {
                        binding.personOne.text = it[apt.roomates[0]]
                        binding.personTwo.text = it[apt.roomates[1]]
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
                    dataViewModel.observeAllRoomates().observe(viewLifecycleOwner) {
                        binding.personOne.text = it[apt.roomates[0]]
                        binding.personTwo.text = it[apt.roomates[1]]
                        binding.personThree.text = it[apt.roomates[2]]

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
                    dataViewModel.observeAllRoomates().observe(viewLifecycleOwner) {
                        binding.personOne.text = it[apt.roomates[0]]
                        binding.personTwo.text = it[apt.roomates[1]]
                        binding.personThree.text = it[apt.roomates[2]]
                        binding.personFour.text = it[apt.roomates[3]]

                    }

                }
                else -> println("Error")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}