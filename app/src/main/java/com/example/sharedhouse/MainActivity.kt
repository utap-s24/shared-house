package com.example.sharedhouse

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.sharedhouse.databinding.ActivityMainBinding
import com.example.sharedhouse.R
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private val authViewModel: AuthViewModel by viewModels()

    private val signInLauncher =
        registerForActivityResult(FirebaseAuthUIActivityResultContract()) {
            authViewModel.updateUser()
        }

    private fun initMenu() {
        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // This could be replaced with return false, but I wanted to show
                // the usual structure for a menu item
                return when (menuItem.itemId) {
                    R.id.action_settings -> {
                        navController.navigate(R.id.settingsFragment)
                        true
                    }
                    else -> false
                }
            }
        })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AuthInit(authViewModel, signInLauncher)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initMenu()

        val navView: BottomNavigationView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_payments, R.id.navigation_items, R.id.navigation_feed, R.id.navigation_profile
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }
    // navigateUp:
    // If we came here from within the app, pop the back stack
    // If we came here from another app, return to it.
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}