package com.sppProject.app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sppProject.app.UserNavActions
import com.sppProject.app.api_integration.fetchers.BuyerFetcher
import com.sppProject.app.data.UserSessionManager
import com.sppProject.app.data.data_class.Buyer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel (
    private val navActions: UserNavActions,
    private val buyerFetcher: BuyerFetcher,
    private val userSessionManager: UserSessionManager
) : ViewModel() {

    private val _buyerState = MutableStateFlow<Buyer?>(null)
    val buyerState: StateFlow<Buyer?> get() = _buyerState

    // Load the logged-in buyer information
    fun loadBuyer() {
        viewModelScope.launch {
            _buyerState.value = userSessionManager.getLoggedInBuyer()
        }
    }

    // Login method for fetching a buyer from the database
    fun login(name: String) {
        viewModelScope.launch {
            try {
                // Attempt to fetch the buyer using the name
                val buyers = buyerFetcher.fetchBuyers()
                val fetchedBuyer = buyers.find { it.name == name } // Modify as per your search criteria

                if (fetchedBuyer != null) {
                    userSessionManager.saveBuyerInfo(fetchedBuyer) // Save to session
                    _buyerState.value = fetchedBuyer // Update state
                } else {
                    // Handle case where buyer is not found (e.g., show an error)
                    _buyerState.value = null // or handle accordingly
                }
            } catch (e: Exception) {
                // Handle exceptions, e.g., network issues
                _buyerState.value = null // or handle accordingly
            }
        }
    }

    // Logout method to clear session
    fun logout() {
        userSessionManager.clearBuyerInfo() // Clear session info
        _buyerState.value = null // Update state
        navActions.navigateToLogin() // Navigate to login screen
    }
}