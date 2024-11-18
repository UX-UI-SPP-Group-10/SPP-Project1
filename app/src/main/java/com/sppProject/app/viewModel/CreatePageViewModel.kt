package com.sppProject.app.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sppProject.app.UserNavActions
import com.sppProject.app.api_integration.fetchers.BuyerFetcher
import com.sppProject.app.api_integration.fetchers.CompanyFetcher
import com.sppProject.app.data.data_class.Buyer
import com.sppProject.app.data.data_class.Company
import com.sppProject.app.view.CreatePageState
import kotlinx.coroutines.launch

class CreatePageViewModel(
    private val buyerFetcher: BuyerFetcher,
    private val companyFetcher: CompanyFetcher,
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : ViewModel() {

    private val _createPageState = mutableStateOf<CreatePageState>(CreatePageState.ShowUser())
    val createPageState: CreatePageState get() = _createPageState.value // read only

    private val _feedbackMessage = mutableStateOf("")
    val feedbackMessage: String get() = _feedbackMessage.value // read only

    var userName: String? = null
    var location: String? = null
    var userMail: String? = null
    var password: String? = null

    // Function to set create page state
    fun setCreatePageState(state: CreatePageState) {
        _createPageState.value = state
    }

    // Function to send information
    fun sendInfo(navActions: UserNavActions) {
        viewModelScope.launch {
            try {
                // Validate fields
                validateFields(userName, userMail, password)

                when (_createPageState.value) {
                    is CreatePageState.ShowUser -> {
                        createFirebaseUser(userMail!!, password!!) {
                            saveToFirestore(
                                collection = "users",
                                data = mapOf(
                                    "name" to userName!!,
                                    "email" to userMail!!,
                                    "role" to "user"
                                )
                            )
                            _feedbackMessage.value = "User added successfully!"
                            navActions.navigateToLogin()
                        }
                    }
                    is CreatePageState.ShowRetailer -> {
                        createFirebaseUser(userMail!!, password!!) {
                            saveToFirestore(
                                collection = "retailers",
                                data = mapOf(
                                    "name" to userName!!,
                                    "email" to userMail!!,
                                    "location" to location!!
                                )
                            )
                            _feedbackMessage.value = "Retailer added successfully!"
                            navActions.navigateToLogin()
                        }
                    }
                    CreatePageState.None -> {
                        _feedbackMessage.value = "No action selected."
                    }
                }
            } catch (e: Exception) {
                _feedbackMessage.value = e.message ?: "An error occurred while adding the profile."
            }
        }
    }

    private fun createFirebaseUser(email: String, password: String, onSuccess: () -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    _feedbackMessage.value = task.exception?.message ?: "Failed to create user."
                }
            }
    }

    private fun saveToFirestore(collection: String, data: Map<String, Any>) {
        firestore.collection(collection)
            .add(data)
            .addOnSuccessListener {
                _feedbackMessage.value = "Profile saved successfully!"
            }
            .addOnFailureListener { e ->
                _feedbackMessage.value = e.message ?: "Failed to save profile."
            }
    }

    private fun validateFields(vararg fields: String?) {
        fields.forEach { field ->
            if (field.isNullOrBlank()) throw IllegalArgumentException("All fields are required!")
        }
    }
}
