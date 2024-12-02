package com.example.apk_administration.ui.theme.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _authState = MutableLiveData<FirebaseUser?>()
    val authState: LiveData<FirebaseUser?> get() = _authState

    init {
        _authState.value = auth.currentUser
    }

    fun signIn(email: String, password: String) {
        _isLoading.value = true
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful) {
                    _authState.value = auth.currentUser
                } else {
                    Log.e("AuthViewModel", "Error: ${task.exception?.message}")
                }
            }
    }

    fun signUp(email: String, password: String) {
        _isLoading.value = true
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val userData = hashMapOf("email" to email, "uid" to user?.uid)
                    firestore.collection("users").document(user?.uid ?: "").set(userData)
                    _authState.value = user
                } else {
                    Log.e("AuthViewModel", "Error: ${task.exception?.message}")
                }
            }
    }

    fun signOut() {
        auth.signOut()
        _authState.value = null
    }
}

