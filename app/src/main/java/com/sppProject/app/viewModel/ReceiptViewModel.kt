package com.sppProject.app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sppProject.app.UserNavActions
import com.sppProject.app.model.api_integration.fetchers.ReceiptFetcher
import com.sppProject.app.model.data.data_class.Item
import com.sppProject.app.model.data.data_class.Receipt
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReceiptViewModel(
    public val userNavActions: UserNavActions,
    private val receiptFetcher: ReceiptFetcher)
    : ViewModel(){
    private var _receiptState = MutableStateFlow<Receipt?>(null)
    var receiptState: StateFlow<Receipt?> = _receiptState

    // Fetch the receipt by ID
    fun fetchReceipt(IdReceipt: Long) {
        viewModelScope.launch {
            try {
                val receipt = receiptFetcher.fetchReceiptById(IdReceipt)
                _receiptState.value = receipt
            } catch (e: Exception) {
                _receiptState.value = null
            }
        }
    }
}