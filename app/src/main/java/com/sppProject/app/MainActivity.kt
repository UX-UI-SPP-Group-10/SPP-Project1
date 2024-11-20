package com.sppProject.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.sppProject.app.api_integration.RetrofitClient
import com.sppProject.app.api_integration.api_service.BuyerApiService
import com.sppProject.app.api_integration.api_service.CompanyApiService
import com.sppProject.app.api_integration.api_service.ItemApiService
import com.sppProject.app.api_integration.api_service.ReceiptApiService
import com.sppProject.app.api_integration.fetchers.BuyerFetcher
import com.sppProject.app.api_integration.fetchers.CompanyFetcher
import com.sppProject.app.api_integration.fetchers.ItemFetcher
import com.sppProject.app.api_integration.fetchers.ReceiptFetcher


class MainActivity : ComponentActivity() {

    private lateinit var buyerFetcher: BuyerFetcher
    private lateinit var companyFetcher: CompanyFetcher
    private lateinit var itemFetcher: ItemFetcher
    private lateinit var receiptFetcher: ReceiptFetcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize BuyerFetcher with the created ApiService
        buyerFetcher = BuyerFetcher(RetrofitClient.createApiService(BuyerApiService::class.java))
        companyFetcher = CompanyFetcher(RetrofitClient.createApiService(CompanyApiService::class.java))
        itemFetcher = ItemFetcher(RetrofitClient.createApiService(ItemApiService::class.java))
        receiptFetcher = ReceiptFetcher(RetrofitClient.createApiService(ReceiptApiService::class.java))

        // Set the content of the activity
        setContent {
            val navController = rememberNavController() // Create NavHostController
            AppNavGraph(navController, buyerFetcher, companyFetcher, itemFetcher, receiptFetcher) // Start the navigation graph
        }
    }
}
