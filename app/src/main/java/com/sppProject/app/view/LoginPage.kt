package com.sppProject.app.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.sppProject.app.api_integration.fetchers.BuyerFetcher

@Composable
fun LoginPage(buyerFetcher: BuyerFetcher) {
    var logOn by remember { mutableStateOf(false) }
    var newUser by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    if (!newUser) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center)
            ) {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Enter Username") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Enter Password") },
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        logOn = true
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Log in")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Or",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { newUser = true },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Create profile")
                }
            }
        }
    } else {
        CreatePage(backToLogin = { newUser = false }, buyerFetcher)
    }
}

