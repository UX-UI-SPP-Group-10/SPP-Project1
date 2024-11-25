package com.sppProject.app.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.sppProject.app.UserNavActions
import com.sppProject.app.model.api_integration.fetchers.BuyerFetcher
import com.sppProject.app.model.api_integration.fetchers.CompanyFetcher
import com.sppProject.app.model.data.UserSessionManager
import com.sppProject.app.model.data.data_class.Buyer
import com.sppProject.app.model.data.data_class.Company
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val navActions: UserNavActions,
    private val buyerFetcher: BuyerFetcher,
    private val companyFetcher: CompanyFetcher,
    val userSessionManager: UserSessionManager
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

    init {
        // Load session as soon as the ViewModel is created
        viewModelScope.launch {
            loadSessionOnStartup()
        }
    }

    fun setUserType(userType: UserType) {
        _userType.value = userType
        loadSession()  // Automatically load session based on the user type
    }

    // old fetcher method, changed to fetchOrCreateUserProfile
    fun fetchUserProfile() {
        viewModelScope.launch {
            val firebaseUser = FirebaseAuth.getInstance().currentUser
            if (firebaseUser == null) return@launch

            try {
                _buyerState.value = buyerFetcher.fetchBuyers().find { it.firebaseUid == firebaseUser.uid }
                _companyState.value = companyFetcher.fetchCompanies().find { it.firebaseUid == firebaseUser.uid }

                _buyerState.value?.let {userSessionManager.saveBuyerInfo(it)}
                _companyState.value?.let {userSessionManager.saveCompanyInfo(it)}
            } catch (e: Exception) {
                _buyerState.value = null
                _companyState.value = null
            }
        }
    }

    fun fetchOrCreateUserProfile() {
        viewModelScope.launch {
            val firebaseUser = FirebaseAuth.getInstance().currentUser
            if (firebaseUser == null) return@launch
            Log.d("FetchOrCreateUserProfile", "Sending info for user: ${firebaseUser.email} ${firebaseUser.uid}")

            try {
                // Fetch all buyers and companies
                val allBuyers = buyerFetcher.fetchBuyers()
                val allCompanies = companyFetcher.fetchCompanies()

                // Check if the user exists as a Buyer
                val existingBuyer = allBuyers.find { it.firebaseUid == firebaseUser.uid }
                if (existingBuyer != null) {
                    _buyerState.value = existingBuyer
                    userSessionManager.saveBuyerInfo(existingBuyer)
                } else {
                    // If not, create a new Buyer
                    val newBuyer = Buyer(
                        name = firebaseUser.displayName ?: "No Buyer Name",
                        firebaseUid = firebaseUser.uid
                    )
                    val createdBuyer = buyerFetcher.createBuyer(newBuyer)
                    _buyerState.value = createdBuyer
                    userSessionManager.saveBuyerInfo(createdBuyer)
                }

                // Check if the user exists as a Company
                val existingCompany = allCompanies.find { it.firebaseUid == firebaseUser.uid }
                if (existingCompany != null) {
                    _companyState.value = existingCompany
                    userSessionManager.saveCompanyInfo(existingCompany)
                } else {
                    // If not, create a new Company
                    val newCompany = Company(
                        name = firebaseUser.displayName ?: "No Company Name",
                        firebaseUid = firebaseUser.uid
                    )
                    val createdCompany = companyFetcher.createCompany(newCompany)
                    _companyState.value = createdCompany
                    userSessionManager.saveCompanyInfo(createdCompany)
                }

            } catch (e: Exception) {
                // Log and handle the exception
                _buyerState.value = null
                _companyState.value = null
            }
        }
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
    fun login(name: String) {
        when (_userType.value) {
            UserType.BUYER -> loginBuyer(name)
            UserType.COMPANY -> loginCompany(name)
            else -> { /* Handle if needed */ }
        }
    }

    // Separate buyer login function
    private fun loginBuyer(name: String) {
        viewModelScope.launch {
            try {
                val buyers = buyerFetcher.fetchBuyers()
                val fetchedBuyer = buyers.find { it.name == name }
                Log.d("UserViewModel", "Fetched buyer: ${fetchedBuyer?.name}")
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
    }

    // Separate company login function
    private fun loginCompany(name: String) {
        viewModelScope.launch {
            try {
                val companies = companyFetcher.fetchCompanies()
                val fetchedCompany = companies.find { it.name == name }
                if (fetchedCompany != null) {
                    userSessionManager.saveCompanyInfo(fetchedCompany)
                    _companyState.value = fetchedCompany
                    _buyerState.value = null
                    Log.d("UserViewModel", "Company login successful: ${fetchedCompany.name}")
                } else {
                    _companyState.value = null // Company not found
                    Log.d("UserViewModel", "Company login failed: company not found")
                }
            } catch (e: Exception) {
                _companyState.value = null
                Log.d("UserViewModel", "Company login failed with exception: ${e.message}")
            }
        }
    }


    private suspend fun loadSessionOnStartup() {
        val buyer = userSessionManager.getLoggedInBuyer()
        val company = userSessionManager.getLoggedInCompany()

        if (buyer != null) {
            _buyerState.value = buyer
            _userType.value = UserType.BUYER
            Log.d("UserViewModel", "Loaded buyer session: ${buyer.name}")
        } else if (company != null) {
            _companyState.value = company
            _userType.value = UserType.COMPANY
            Log.d("UserViewModel", "Loaded company session: ${company.name}")
        } else {
            Log.d("UserViewModel", "No user session found")
        }
    }

    // Logout method to clear session
    fun logout() {
        userSessionManager.clearSessionInfo()
        _buyerState.value = null
        _companyState.value = null
        navActions.navigateToLogin()
    }

}