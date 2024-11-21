package com.sppProject.app.api_integration.fetchers

import com.sppProject.app.api_integration.api_service.BuyerApiService
import com.sppProject.app.data.data_class.Buyer
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

    // Fetch buyers using the correct method name
    suspend fun fetchBuyers(): List<Buyer> {
        // Correct API method name
        val response = buyerApiService.getAllBuyers()

        // Handle response
        if (response.isNotEmpty()) {
            println("Buyers fetched: $response")  // Debugging output
            return response
        } else {
            println("No buyers found.")
            return emptyList()  // Return empty list if no buyers are found
        }
    }

    // Create buyer using the generic method
    suspend fun createBuyer(newBuyer: Buyer): Buyer {
        return apiFetcher.handleApiCallSingle { buyerApiService.createBuyer(newBuyer) }
    }

    suspend fun getBuyerById(id: Long): Buyer {
        return apiFetcher.handleApiCallSingle { buyerApiService.getBuyerById(id) }
    }
}

