package com.sppProject.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.sppProject.app.api_integration.RetrofitClient
import com.sppProject.app.api_integration.api_service.BuyerApiService
import com.sppProject.app.api_integration.api_service.CompanyApiService
import com.sppProject.app.api_integration.api_service.ItemApiService
import com.sppProject.app.api_integration.api_service.ReceiptApiService
import com.sppProject.app.api_integration.fetchers.BuyerFetcher
import com.sppProject.app.api_integration.fetchers.CompanyFetcher
import com.sppProject.app.api_integration.fetchers.ItemFetcher
import com.sppProject.app.data.UserSessionManager
import com.sppProject.app.api_integration.fetchers.ReceiptFetcher
import com.sppProject.app.ui.theme.SPPProjectTheme


class MainActivity : ComponentActivity() {

    private lateinit var buyerFetcher: BuyerFetcher
    private lateinit var companyFetcher: CompanyFetcher
    private lateinit var itemFetcher: ItemFetcher
    private lateinit var userSessionManager: UserSessionManager
    private lateinit var receiptFetcher: ReceiptFetcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this) // Initialize Firebase

        userSessionManager = UserSessionManager(this)

        // Initialize BuyerFetcher with the created ApiService
        // Initialize fetchers
        buyerFetcher = BuyerFetcher(RetrofitClient.createApiService(BuyerApiService::class.java))
        companyFetcher = CompanyFetcher(RetrofitClient.createApiService(CompanyApiService::class.java))
        itemFetcher = ItemFetcher(RetrofitClient.createApiService(ItemApiService::class.java))
        receiptFetcher = ReceiptFetcher(RetrofitClient.createApiService(ReceiptApiService::class.java))

        setContent {
            val navController = rememberNavController()

            // Wrapping the entire app in AppTheme to apply the correct colors
            SPPProjectTheme {
                AppNavGraph(
                    navController = navController,
                    buyerFetcher = buyerFetcher,
                    companyFetcher = companyFetcher,
                    itemFetcher = itemFetcher,
                    receiptFetcher = receiptFetcher
                )
            }
        }
    }
}
