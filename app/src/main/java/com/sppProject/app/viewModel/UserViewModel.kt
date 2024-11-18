package com.sppProject.app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.sppProject.app.UserNavActions
import com.sppProject.app.api_integration.fetchers.BuyerFetcher
import com.sppProject.app.api_integration.fetchers.CompanyFetcher
import com.sppProject.app.data.UserSessionManager
import com.sppProject.app.data.data_class.Buyer
import com.sppProject.app.data.data_class.Company
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val navActions: UserNavActions,
    private val buyerFetcher: BuyerFetcher,
    private val companyFetcher: CompanyFetcher,
    private val userSessionManager: UserSessionManager,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _buyerState = MutableStateFlow<Buyer?>(null)
    val buyerState: StateFlow<Buyer?> get() = _buyerState

    private val _companyState = MutableStateFlow<Company?>(null)
    val companyState: StateFlow<Company?> get() = _companyState

    private val _userType = MutableStateFlow<UserType?>(UserType.BUYER)
    val userType: StateFlow<UserType?> get() = _userType

    enum class UserType {
        BUYER, COMPANY
    }

    fun setUserType(userType: UserType) {
        _userType.value = userType
        loadSession()  // Automatically load session based on the user type
    }

    // Load session based on the current user type
    private fun loadSession() {
        viewModelScope.launch {
            when (_userType.value) {
                UserType.BUYER -> {
                    _buyerState.value = userSessionManager.getLoggedInBuyer()
                    _companyState.value = null
                }
                UserType.COMPANY -> {
                    _companyState.value = userSessionManager.getLoggedInCompany()
                    _buyerState.value = null
                }
                else -> { /* Handle if needed */ }
            }
        }
    }

    // Unified login method based on the stored user type
    fun login(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = firebaseAuth.currentUser
                    if (firebaseUser != null) {
                        userSessionManager.saveFirebaseUser(firebaseUser.uid, firebaseUser.email)
                        viewModelScope.launch {
                            when (_userType.value) {
                                UserType.BUYER -> fetchBuyerProfile(email)
                                UserType.COMPANY -> fetchCompanyProfile(email)
                                else -> { /* Handle unknown user type */ }
                            }
                        }
                    }
                } else {
                    // Handle login failure
                    _buyerState.value = null
                    _companyState.value = null
                }
            }
    }

    fun signUp(email: String, password: String, name: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = firebaseAuth.currentUser
                    if (firebaseUser != null) {
                        userSessionManager.saveFirebaseUser(firebaseUser.uid, firebaseUser.email)
                        viewModelScope.launch {
                            when (_userType.value) {
                                UserType.BUYER -> saveBuyerProfile(name, email)
                                UserType.COMPANY -> saveCompanyProfile(name, email)
                                else -> { /* Handle unknown user type */ }
                            }
                        }
                    }
                } else {
                    // Handle sign-up failure
                    _buyerState.value = null
                    _companyState.value = null
                }
            }
    }
    private suspend fun saveBuyerProfile(name: String, mail: String) {
        try {
            val newBuyer = Buyer(id = 0, name = name, mail = mail) // Customize as per your model
            userSessionManager.saveBuyerInfo(newBuyer)
            _buyerState.value = newBuyer
        } catch (e: Exception) {
            _buyerState.value = null
        }
    }

    // Save company profile during sign-up
    private suspend fun saveCompanyProfile(name: String, mail: String) {
        try {
            val newCompany = Company(id = 0, name = name, mail = mail) // Customize as per your model
            userSessionManager.saveCompanyInfo(newCompany)
            _companyState.value = newCompany
        } catch (e: Exception) {
            _companyState.value = null
        }
    }

    private suspend fun fetchBuyerProfile(email: String) {
        try {
            val buyers = buyerFetcher.fetchBuyers()
            val fetchedBuyer = buyers.find { it.mail == email } // Assuming Buyer has an email field
            if (fetchedBuyer != null) {
                userSessionManager.saveBuyerInfo(fetchedBuyer)
                _buyerState.value = fetchedBuyer
                _companyState.value = null
            } else {
                _buyerState.value = null // Buyer not found
            }
        } catch (e: Exception) {
            _buyerState.value = null
        }
    }

    // Fetch company profile after successful Firebase login
    private suspend fun fetchCompanyProfile(email: String) {
        try {
            val companies = companyFetcher.fetchCompanies()
            val fetchedCompany = companies.find { it.mail == email } // Assuming Company has an email field
            if (fetchedCompany != null) {
                userSessionManager.saveCompanyInfo(fetchedCompany)
                _companyState.value = fetchedCompany
                _buyerState.value = null
            } else {
                _companyState.value = null // Company not found
            }
        } catch (e: Exception) {
            _companyState.value = null
        }
    }


    // Logout method to clear session
    fun logout() {
        firebaseAuth.signOut()
        userSessionManager.clearSessionInfo()
        _buyerState.value = null
        _companyState.value = null
        navActions.navigateToLogin()
    }
}