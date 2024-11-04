package com.sppProject.app.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sppProject.app.UserNavActions

@Composable
fun RetailerHomePage(userNavActions: UserNavActions) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to the Retailer Home Page")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp) // Optional padding from the edges
    ) {
        Button(
            onClick = {
                // Logic to create a listing goes here
            },
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Text("Create Listing")
        }

        Button(
            onClick = {
                userNavActions.navigateBack() // Use NavController to go back
            },
            modifier = Modifier.align(Alignment.BottomStart)
        ) {
            Text("Back")
        }
    }
}