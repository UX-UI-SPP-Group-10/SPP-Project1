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
fun UserHomePage(navActions: UserNavActions) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to the User Home Page")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp) // Optional padding from the edges
    ) {
        Button(
            onClick = {
                // add list
            },
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Text("View Listings")
        }

        Button(
            onClick = {
                navActions.navigateBack()
            }, // Use NavController to go back
            modifier = Modifier.align(Alignment.BottomStart)
        ) {
            Text("Back")
        }
    }
}
