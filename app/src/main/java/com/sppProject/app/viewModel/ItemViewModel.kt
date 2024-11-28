package com.sppProject.app.viewModel

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sppProject.app.UserNavActions
import com.sppProject.app.model.api_integration.fetchers.ItemFetcher
import com.sppProject.app.model.api_integration.fetchers.ReceiptFetcher
import com.sppProject.app.model.data.UserSessionManager
import com.sppProject.app.model.data.data_class.Company
import com.sppProject.app.model.data.data_class.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ItemViewModel(
    private val itemFetcher: ItemFetcher,
    private val receiptFetcher: ReceiptFetcher,
    val userViewModel: UserViewModel,
    public val userNavActions: UserNavActions
) : ViewModel() {

    private val _itemList = MutableStateFlow<List<Item>>(emptyList())
    val itemList: StateFlow<List<Item>> get() = _itemList

    private var _itemState = MutableStateFlow<Item?>(null)
    var itemState: StateFlow<Item?> = _itemState

    private val _isReceiptMade = MutableStateFlow(false)
    val isReceiptMade: StateFlow<Boolean> = _isReceiptMade

    private val _noMoreItems = MutableStateFlow(false)
    val noMoreItems: StateFlow<Boolean> = _noMoreItems

    var userType = UserViewModel.UserType.BUYER

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description

    private val _numberOfItem = MutableStateFlow("0")
    val numberOfItem: StateFlow<String> = _numberOfItem

    private val _price = MutableStateFlow("0.0")
    val price: StateFlow<String> = _price

    fun fetchItems() {
        viewModelScope.launch {
            val items = itemFetcher.fetchItems()
            _itemList.value = items
        }
    }

    fun fetchItemsByCompanyID(companyID: Long) {
        viewModelScope.launch {
            try {
                val items = itemFetcher.fetchItemsByCompanyId(companyID)
                _itemList.value = items
            } catch (e: Exception) {
                // Handle error, e.g., show a message
                e.printStackTrace()
            }
        }
    }

    // Fetch the item by ID
    fun fetchItem(itemId: Long) {
        viewModelScope.launch {
            try {
                val item = itemFetcher.getItemById(itemId)
                _itemState.value = item
            } catch (e: Exception) {
                _itemState.value = null
            }
        }
    }

    fun onTitleChange(newTitle: String) {
        _title.value = newTitle
    }

    fun onDescriptionChange(newDescription: String) {
        _description.value = newDescription
    }

    fun onNumberOfItemChange(newNumberOfItem: String) {
        if (newNumberOfItem.all { it.isDigit() }) {
            _numberOfItem.value = newNumberOfItem
        }
    }

    fun onPriceChange(newPrice: String) {
        if (newPrice.matches(Regex("^[0-9]*\\.?[0-9]*$"))) {
            _price.value = newPrice
        }
    }

    fun postItem() {
        val currentCompany = userViewModel.companyState.value
        val item = Item(
            name = _title.value,
            description = _description.value,
            stock = _numberOfItem.value.toIntOrNull() ?: 0,
            price = _price.value.toDoubleOrNull()?.toInt() ?: 0
        )

        viewModelScope.launch {
            itemFetcher.createItem(currentCompany?.id ?: 0, item)
        }

        userNavActions.navigateToRetailerHome()
    }

    fun updateItem(itemId: Long){
        val item = Item(
            name = _title.value,
            description = _description.value,
            stock = _numberOfItem.value.toIntOrNull() ?: 0,
            price = _price.value.toDoubleOrNull()?.toInt() ?: 0
        )
        viewModelScope.launch {
            itemFetcher.updateItem(itemId, item)
        }
        userNavActions.navigateToRetailerHome()
    }

    fun reserveItem(itemId: Long) {
        viewModelScope.launch {
            val item = _itemState.value
            if (item != null && item.stock > 0) {
                receiptFetcher.createReceipt(userViewModel.buyerState.value?.id ?: 0, itemId)
                _isReceiptMade.value = true
            } else {
                _noMoreItems.value = true
            }
        }
        userNavActions.navigateUserHome()
    }

    fun resetReceiptState() {
        _isReceiptMade.value = false
    }
}