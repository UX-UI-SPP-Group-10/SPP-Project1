package com.sppProject.app.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StartPage() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) { Button(
            onClick = {
            },
            modifier = Modifier.width(150.dp)
        ) {
            Text("User login")
        }

            Button(
                onClick = {
                },
                modifier = Modifier.width(150.dp)
            ) {
                Text("Retailer login")
            }

            Button(
                onClick = {
                },
                modifier = Modifier.width(150.dp)
            ) {
                Text("Create profile")
            } }
    }
}