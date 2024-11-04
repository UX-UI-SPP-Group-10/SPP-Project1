package com.sppProject.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.sppProject.app.api_integration.RetrofitClient
import com.sppProject.app.api_integration.api_service.BuyerApiService
import com.sppProject.app.api_integration.api_service.CompanyApiService
import com.sppProject.app.api_integration.fetchers.BuyerFetcher
import com.sppProject.app.api_integration.fetchers.CompanyFetcher
import com.sppProject.app.view.LoginPage

class MainActivity : ComponentActivity() {
    private lateinit var buyerFetcher: BuyerFetcher // Use BuyerFetcher
    private lateinit var companyFetcher: CompanyFetcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize BuyerFetcher with the created ApiService
        buyerFetcher = BuyerFetcher(RetrofitClient.createApiService(BuyerApiService::class.java))
        companyFetcher = CompanyFetcher(RetrofitClient.createApiService(CompanyApiService::class.java))

        // Set the content of the activity
        setContent {
            LoginPage(buyerFetcher, companyFetcher)
        }
    }
}
