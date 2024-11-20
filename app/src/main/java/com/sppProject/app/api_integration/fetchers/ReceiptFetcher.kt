package com.sppProject.app.api_integration.fetchers

import com.sppProject.app.api_integration.ApiFetcher
import com.sppProject.app.api_integration.api_service.ReceiptApiService
import com.sppProject.app.data.data_class.Receipt

class ReceiptFetcher(
    private val receiptApiService: ReceiptApiService
) {
    private val apiFetcher = ApiFetcher<Receipt>(receiptApiService)

    suspend fun fetchReceipts(): List<Receipt> {
        val response = receiptApiService.getAllReceipts()

        if (response.isNotEmpty()) {
            println("Receipts fetched: $response")
            return response
        } else {
            println("No receipts found.")
            return emptyList()
        }
    }

    suspend fun createReceipt(buyerId: Long, itemId: Long): Receipt {
        return apiFetcher.handleApiCallSingle {
            receiptApiService.createReceipt(buyerId, itemId)
        }
    }

    suspend fun fetchReceiptById(id: Long): Receipt {
        return apiFetcher.handleApiCallSingle { receiptApiService.getReceiptById(id) }
    }
}
