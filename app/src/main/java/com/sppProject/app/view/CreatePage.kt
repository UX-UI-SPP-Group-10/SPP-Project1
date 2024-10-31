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
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sppProject.app.api_integration.data_class.Buyer
import com.sppProject.app.api_integration.fetchers.BuyerFetcher

@Composable
fun CreatePage(backToLogin: () -> Unit, buyerFetcher: BuyerFetcher){
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


@Composable
fun UserInfo(
    youserInfomation: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    sendInfo: Boolean,
    buyerFetcher: BuyerFetcher,
    backToLogin: () -> Unit)
{
    var feedbackMessage by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Button(onClick = onClick) {
        Text("User")
    }
    if(youserInfomation){
        Column(modifier = Modifier.padding(top = 16.dp)){
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Enter Uesrname") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Enter Password") },
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (youserInfomation && sendInfo && name.isNotBlank()) {
        LaunchedEffect(sendInfo) {
            try {
                buyerFetcher.createBuyer(Buyer(name = name)) // Create buyer
                name = "" // Clear name input after successful submission
                feedbackMessage = "Buyer added successfully!"
                backToLogin()
            } catch (e: Exception) {
                feedbackMessage = e.message ?: "An error occurred while adding buyer."
            }
        }
    } else if (youserInfomation && sendInfo && name.isBlank()) {
        feedbackMessage = "Name cannot be empty."
    }
}

@Composable
fun RetailerInfo(
    retailerInfomation: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    sendInfo: Boolean,
    buyerFetcher: BuyerFetcher){
    var company by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Button(onClick = onClick) {
        Text("Retailer")
    }

    if(retailerInfomation){
        Column(modifier = Modifier.padding(top = 16.dp)){
            TextField(
                value = company,
                onValueChange = { company = it },
                label = { Text("Enter Company") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Enter Location") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Enter Password") },
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
    if(retailerInfomation && sendInfo){
        //send infomation and gow to logind page
    }
}