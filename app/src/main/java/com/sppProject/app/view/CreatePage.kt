package com.sppProject.app.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sppProject.app.R
import com.sppProject.app.UserNavActions
import com.sppProject.app.data.data_class.Buyer
import com.sppProject.app.api_integration.fetchers.BuyerFetcher

@Composable
fun CreatePage(navActions: UserNavActions, buyerFetcher: BuyerFetcher) {
    var retailerInformation by remember { mutableStateOf(false) }
    var userInformation by remember { mutableStateOf(false) }
    var sendInfo by remember { mutableStateOf(false) }
    var navigateToRetailerHomePage by remember { mutableStateOf(false) }
    var navigateToUserHomePage by remember { mutableStateOf(false) }

    if (navigateToRetailerHomePage) {
        navActions.navigateToRetailerHome()
    } else if (navigateToUserHomePage) {
        navActions.navigateToUserHome()
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
        ) {
            RetailerInfo(
                retailerInformation = retailerInformation,
                onClick = {
                    retailerInformation = !retailerInformation
                    if (retailerInformation) userInformation = false
                },
                sendInfo = sendInfo,
                buyerFetcher = buyerFetcher,
                onNavigateToRetailerHome = { navigateToRetailerHomePage = true }
            )
            Spacer(modifier = Modifier.height(16.dp))
            UserInfo(
                userInformation = userInformation,
                onClick = {
                    userInformation = !userInformation
                    if (userInformation) retailerInformation = false
                },
                sendInfo = sendInfo,
                buyerFetcher = buyerFetcher,
                // Update the backToLogin parameter to use the navigation action
                backToLogin = { navActions.navigateBack() }, // Navigate back using UserNavActions
                onNavigateToUserHome = { navigateToUserHomePage = true }
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

            Button(
                onClick = {
                    navActions.navigateBack() // Navigate back to the login screen using UserNavActions
                },
                modifier = Modifier.align(Alignment.BottomStart)
            ) {
                Icon(painter = painterResource(R.drawable.arrow_left),
                    contentDescription = "Back")
            }
        }
    }
}


@Composable
fun RetailerInfo(
    retailerInformation: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    sendInfo: Boolean,
    buyerFetcher: BuyerFetcher,
    onNavigateToRetailerHome: () -> Unit
){
    var company by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    Button(onClick = onClick) {
        Text("Retailer")
    }


    if(retailerInformation){
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

            Button(onClick = {
                onNavigateToRetailerHome()
            }) {
                Text("Create Profile")
            }

        }
    }

    if(retailerInformation && sendInfo){
        //send infomation and gow to logind page
    }
}

@Composable
fun UserInfo(
    userInformation: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    sendInfo: Boolean,
    buyerFetcher: BuyerFetcher,
    backToLogin: () -> Unit,
    onNavigateToUserHome: () -> Unit)
{
    var feedbackMessage by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Button(onClick = onClick) {
        Text("User")
    }
    if(userInformation){
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

            Button(onClick = {
                onNavigateToUserHome()
            }) {
                Text("Create Profile")
            }
        }
    }

    if (userInformation && sendInfo && name.isNotBlank()) {
        LaunchedEffect(sendInfo) {
            try {
                buyerFetcher.createBuyer(Buyer(id = 0, name = name)) // Create buyer
                name = "" // Clear name input after successful submission
                feedbackMessage = "Buyer added successfully!"
                backToLogin()
            } catch (e: Exception) {
                feedbackMessage = e.message ?: "An error occurred while adding buyer."
            }
        }
    } else if (userInformation && sendInfo && name.isBlank()) {
        feedbackMessage = "Name cannot be empty."
    }
}
