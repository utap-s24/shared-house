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
import com.example.sharedhouse.databinding.SettingsBinding

class SettingsFragment : Fragment() {
    // https://developer.android.com/topic/libraries/view-binding#fragments
    private var _binding: SettingsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // No settings menu in settings fragment
                menu.clear()
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Do nothing when settings is clicked
                return false
            }
        }, viewLifecycleOwner)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMenu()
        binding.meaninglessSwitch.isChecked = false
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}