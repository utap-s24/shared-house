package com.example.sharedhouse

import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

// https://firebase.google.com/docs/auth/android/firebaseui

//Based on flipped classroom.
class AuthInit(viewModel: AuthViewModel, signInLauncher: ActivityResultLauncher<Intent>) {
    companion object {
        private const val TAG = "AuthInit"
        fun setDisplayName(displayName : String, viewModel: AuthViewModel) {
            Log.d(TAG, "XXX profile change request")
            val user = FirebaseAuth.getInstance().currentUser
            val request = UserProfileChangeRequest.Builder()
            request.displayName = displayName

            user!!.updateProfile(request.build())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        viewModel.updateUser()
                    }
                }
        }
    }

    init {
        val user = FirebaseAuth.getInstance().currentUser
        if(user == null) {
            Log.d(TAG, "XXX user null")
            // Choose authentication providers
            val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build())

            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false)
                .setAvailableProviders(providers)
                .build()
            signInLauncher.launch(signInIntent)
        } else {
            Log.d(TAG, "XXX user ${user.displayName} email ${user.email}")
            viewModel.updateUser()
        }
    }
}
