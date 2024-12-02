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

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage
    private val _successMessage = MutableLiveData<String?>()
    val successMessage: LiveData<String?> get() = _successMessage

    init {
        _authState.value = auth.currentUser
    }

    fun signIn(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _errorMessage.value = "Todos los campos son obligatorios."
            return
        }

        _isLoading.value = true
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful) {
                    _authState.value = auth.currentUser
                    _errorMessage.value = null // Resetea el mensaje de error
                } else {
                    _errorMessage.value = task.exception?.message ?: "Error desconocido al iniciar sesión."
                }
            }
    }


    fun clearError() {
        _errorMessage.value = null
    }

    private fun validateFields(
        email: String,
        password: String,
        name: String,
        phone: String
    ): Boolean {
        val errors = mutableMapOf<String, String?>()

        if (email.isEmpty()) {
            errors["email"] = "El correo electrónico es obligatorio."
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errors["email"] = "El correo electrónico no es válido."
        }

        if (password.isEmpty()) {
            errors["password"] = "La contraseña es obligatoria."
        } else if (password.length < 6) {
            errors["password"] = "La contraseña debe tener al menos 6 caracteres."
        }

        if (name.isEmpty()) {
            errors["name"] = "El nombre es obligatorio."
        }

        if (phone.isEmpty()) {
            errors["phone"] = "El teléfono es obligatorio."
        } else if (phone.length != 9 || !phone.all { it.isDigit() }) {
            errors["phone"] = "El teléfono debe tener 9 dígitos y ser numérico."
        }

        _errorMessage.value = errors.toString()

        // Devuelve `true` si no hay errores
        return errors.isEmpty()
    }

    fun signUp(email: String, password: String, name: String, phone: String) {
        if (!validateFields(email, password, name, phone)) return

        _isLoading.value = true
        auth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { methodTask ->
                if (methodTask.isSuccessful) {
                    val signInMethods = methodTask.result?.signInMethods
                    if (!signInMethods.isNullOrEmpty()) {
                        // El correo ya está registrado
                        _isLoading.value = false
                        _errorMessage.value = "El correo electrónico ya está registrado. Por favor, usa otro."
                    } else {
                        // El correo no está registrado, procede con el registro
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                _isLoading.value = false
                                if (task.isSuccessful) {
                                    val user = auth.currentUser
                                    _authState.value = user
                                    val userData = hashMapOf(
                                        "email" to email,
                                        "uid" to user?.uid,
                                        "name" to name,
                                        "phone" to phone
                                    )
                                    firestore.collection("users").document(user?.uid ?: "").set(userData)
                                        .addOnSuccessListener {
                                            _successMessage.value = "Registro exitoso."
                                        }
                                        .addOnFailureListener { e ->
                                            _errorMessage.value = "Error al guardar los datos: ${e.message}"
                                        }
                                } else {
                                    _errorMessage.value = task.exception?.message ?: "Error desconocido al registrar usuario."
                                }
                            }
                    }
                } else {
                    // Error al verificar si el correo está registrado
                    _isLoading.value = false
                    _errorMessage.value = "Error al verificar el correo: ${methodTask.exception?.message}"
                }
            }
    }


    fun clearSuccessMessage() {
        _successMessage.value = null
    }

    fun signOut() {
        auth.signOut()
        _authState.value = null
    }
}

