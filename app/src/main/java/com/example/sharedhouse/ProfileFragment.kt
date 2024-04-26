package com.example.sharedhouse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.sharedhouse.databinding.ProfileViewBinding
import com.example.sharedhouse.db.MainViewModel
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract

class ProfileFragment : Fragment() {
    companion object {
        val TAG : String = ProfileFragment::class.java.simpleName
    }

    private val signInLauncher =
        registerForActivityResult(FirebaseAuthUIActivityResultContract()) {
            authViewModel.updateUser()
        }

    private var _binding: ProfileViewBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by activityViewModels()
    private val dataViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProfileViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // XXX Write me. Observe data to display in UI
        authViewModel.observeEmail().observe(viewLifecycleOwner) {
            binding.emailText.text = it
        }

        authViewModel.observeDisplayName().observe(viewLifecycleOwner) {
            binding.nameText.text = it
        }

        binding.apartmentButton.setOnClickListener {

        }

        binding.edit.setOnClickListener {

        }

        binding.logout.setOnClickListener {
            authViewModel.signOut()
            AuthInit(authViewModel, signInLauncher)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}