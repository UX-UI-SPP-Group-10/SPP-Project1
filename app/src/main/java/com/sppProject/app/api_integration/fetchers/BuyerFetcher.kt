package com.sppProject.app.api_integration.fetchers

import com.sppProject.app.api_integration.api_service.BuyerApiService
import com.sppProject.app.api_integration.data_class.Buyer
import com.sppProject.app.api_integration.ApiFetcher


/**
 * This class handles API calls related to buyers.
 * It provides methods to get and create buyers.
 *
 * @param buyerApiService The API service for buyer-related requests.
 */
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

