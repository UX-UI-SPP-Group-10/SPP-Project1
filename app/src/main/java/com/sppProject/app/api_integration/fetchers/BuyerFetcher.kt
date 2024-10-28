package com.sppProject.app.api_integration.fetchers

import com.sppProject.app.api_integration.api_service.BuyerApiService
import com.sppProject.app.api_integration.data_class.Buyer
import com.sppProject.app.api_integration.ApiFetcher

class BuyerFetcher(
    private val buyerApiService: BuyerApiService
) {
    private val apiFetcher = ApiFetcher<Buyer>(buyerApiService)

    // Fetch buyers using the generic method
    suspend fun fetchBuyers(): List<Buyer> {
        return apiFetcher.handleApiCallList { buyerApiService.getAllBuyers() }
    }

    // Create buyer using the generic method
    suspend fun createBuyer(item: Buyer): Buyer {
        return apiFetcher.handleApiCallSingle { buyerApiService.createBuyer(item) }
    }
}

