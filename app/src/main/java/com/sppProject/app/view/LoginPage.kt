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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.sppProject.app.NavigationRoutes
import com.sppProject.app.UserNavActions
import com.sppProject.app.api_integration.fetchers.BuyerFetcher
import com.sppProject.app.viewModel.UserViewModel

@Composable
fun LoginPage(
    userViewModel: UserViewModel,
    navActions: UserNavActions
) {
    var newUser by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }

    // Observe the buyer state
    val buyerState by userViewModel.buyerState.collectAsState()

    // If a buyer is logged in, navigate to the user home
    if (buyerState != null) {
        navActions.navigateToUserHome()
    }
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

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        userViewModel.login(name) // Call login method
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
                    onClick = {
                        navActions.navigateToCreatePage()
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Create profile")
                }
            }
            Button(
                onClick = {
                    navActions.navigateBack()
                },
                modifier = Modifier.align(Alignment.BottomStart)
            ) {
                Text("Back")
            }
        }
}

