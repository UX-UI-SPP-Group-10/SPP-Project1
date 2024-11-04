package com.sppProject.app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sppProject.app.api_integration.fetchers.BuyerFetcher
import com.sppProject.app.data.UserSessionManager
import com.sppProject.app.data.data_class.Buyer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel (
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

    // Login method for creating a buyer and saving to session
    fun login(name: String) {
        viewModelScope.launch {
            val newBuyer = Buyer(name) // Create new Buyer instance
            val createdBuyer = buyerFetcher.createBuyer(newBuyer) // Use BuyerFetcher to create
            userSessionManager.saveBuyerInfo(createdBuyer) // Save to session
            _buyerState.value = createdBuyer // Update state
        }
    }

    // Logout method to clear session
    fun logout() {
        userSessionManager.clearBuyerInfo() // Clear session info
        _buyerState.value = null // Update state
    }
    }