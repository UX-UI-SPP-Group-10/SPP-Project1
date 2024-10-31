package com.sppProject.app.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sppProject.app.api_integration.fetchers.BuyerFetcher

@Composable
fun App(backToLogin: () -> Unit, buyerFetcher: BuyerFetcher){
    var retailerInfomation by remember { mutableStateOf(false) }
    var youserInfomation by remember { mutableStateOf(false) }
    var sendInfo by remember { mutableStateOf(false) }
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 32.dp))
    {
        RetailerInfo(
            retailerInfomation = retailerInfomation,
            onClick = {
                retailerInfomation = !retailerInfomation
                if(retailerInfomation) youserInfomation = false},
            sendInfo = sendInfo,
            buyerFetcher = buyerFetcher
        )
        Spacer(modifier = Modifier.height(16.dp))
        UserInfo(
            youserInfomation = youserInfomation,
            onClick = {
                youserInfomation = !youserInfomation
                if (youserInfomation) retailerInfomation = false
            },
            sendInfo = sendInfo,
            buyerFetcher = buyerFetcher,
            backToLogin = backToLogin
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp) // Optional padding from the edges
    ) {
        Button(
            onClick = {
                sendInfo = true
            },
            modifier = Modifier.align(Alignment.BottomEnd) // Aligns the button to the bottom-right
        ) {
            Text("Make Profile")
        }

        Button(onClick = backToLogin,
            modifier = Modifier.align(Alignment.BottomStart)){
            Text("Back")
        }
    }
}