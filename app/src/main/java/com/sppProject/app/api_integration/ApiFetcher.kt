package com.api_integration

import com.api_integration.api_service.BuyerApiService
import com.api_integration.data_class.Buyer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiFetcher(
    private val buyerApiService: BuyerApiService,
) {

    // Fetch buyers using the generic method
    fun fetchBuyers(onFetched: (List<Buyer>, String?) -> Unit) {
        val call = buyerApiService.getAllBuyers()
        handleApiCall(call,
            onSuccess = { buyers ->
                onFetched(buyers, null) // Success
            },
            onError = { errorMsg ->
                onFetched(emptyList(), errorMsg) // Handle error
            }
        )
    }

    // Create buyer using the generic method
    fun createBuyer(item: Buyer, onCreated: (Buyer?, String?) -> Unit) {
        val call = buyerApiService.createBuyer(item)
        handleApiCall(call,
            onSuccess = { buyer ->
                onCreated(buyer, null) // Success
            },
            onError = { errorMsg ->
                onCreated(null, errorMsg) // Handle error
            }
        )
    }
}

