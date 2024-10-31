package com.sppProject.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sppProject.app.api_integration.RetrofitClient
import com.sppProject.app.api_integration.fetchers.BuyerFetcher
import com.sppProject.app.api_integration.data_class.Buyer
import com.sppProject.app.api_integration.api_service.BuyerApiService
import com.sppProject.app.view.LoginPage
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var buyerFetcher: BuyerFetcher // Use BuyerFetcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize BuyerFetcher with the created ApiService
        buyerFetcher = BuyerFetcher(RetrofitClient.createApiService(BuyerApiService::class.java))

        // Set the content of the activity
        setContent {
            LoginPage(buyerFetcher)
        }
    }
}
