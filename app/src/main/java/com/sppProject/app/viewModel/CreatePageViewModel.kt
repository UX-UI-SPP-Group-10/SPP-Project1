package com.sppProject.app.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sppProject.app.UserNavActions
import com.sppProject.app.api_integration.fetchers.BuyerFetcher
import com.sppProject.app.data.data_class.Buyer
import com.sppProject.app.view.CreatePageState
import kotlinx.coroutines.launch

class CreatePageViewModel(private val buyerFetcher: BuyerFetcher) : ViewModel() {

    private val _createPageState = mutableStateOf<CreatePageState>(CreatePageState.ShowUser())
    val createPageState: CreatePageState get() = _createPageState.value // read only

    private val _feedbackMessage = mutableStateOf("")
    val feedbackMessage: String get() = _feedbackMessage.value // read only

    var userName: String? = null

    // Function to set create page state
    fun setCreatePageState(state: CreatePageState) {
        _createPageState.value = state
    }

    // Function to send information
    fun sendInfo(navActions: UserNavActions) {
        viewModelScope.launch {
            try {
                when (_createPageState.value) {
                    is CreatePageState.ShowUser -> {
                        buyerFetcher.createBuyer(Buyer(userName.toString())) // Example name
                        _feedbackMessage.value = "User added successfully!"
                        navActions.navigateToLogin()
                    }
                    is CreatePageState.ShowRetailer -> {
                        buyerFetcher.createBuyer(Buyer(userName.toString())) // Example name
                        _feedbackMessage.value = "Retailer added successfully!"
                        navActions.navigateToLogin()
                    }
                    CreatePageState.None -> TODO()
                }
            } catch (e: Exception) {
                _feedbackMessage.value = e.message ?: "An error occurred while adding user."
            }
        }
    }
}
