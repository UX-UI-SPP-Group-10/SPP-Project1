package com.sppProject.app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val userSessionManager: UserSessionManager
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
                } else {
                    _companyState.value = null // Company not found
                }
            } catch (e: Exception) {
                _companyState.value = null
            }
        }
    }

    // Logout method to clear session
    fun logout() {
        userSessionManager.clearSessionInfo()
        _buyerState.value = null
        _companyState.value = null
        _userType.value = null
        navActions.navigateToLogin()
    }
}