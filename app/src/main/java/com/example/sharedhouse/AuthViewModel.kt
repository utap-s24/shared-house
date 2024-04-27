package com.example.sharedhouse

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

//Based on flipped classroom
class AuthViewModel : ViewModel() {
    private var displayName = MutableLiveData("Uninitialized")
    private var email = MutableLiveData("Uninitialized")
    private var uid = MutableLiveData("Uninitialized")

    private fun userLogout() {
        displayName.postValue("No user")
        email.postValue("No email, no active user")
        uid.postValue("No uid, no active user")

    }

    fun updateUser() {
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            displayName.postValue(currentUser.displayName ?: "Uninitialized")
            email.postValue(currentUser.email ?: "Uninitialized")
            uid.postValue(currentUser.uid)
        }
    }

    fun observeDisplayName() : LiveData<String> {
        return displayName
    }
    fun observeEmail() : LiveData<String> {
        return email
    }
    fun observeUid() : LiveData<String> {
        return uid
    }
    fun signOut() {
        FirebaseAuth.getInstance().signOut()
        userLogout()
    }
}