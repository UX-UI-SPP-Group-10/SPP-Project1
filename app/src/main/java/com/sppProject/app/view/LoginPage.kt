package com.sppProject.app.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sppProject.app.UserNavActions
import com.sppProject.app.view.components.CustomButton
import com.sppProject.app.viewModel.UserViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(
    userViewModel: UserViewModel,
    navActions: UserNavActions
) {
    var name by remember { mutableStateOf("") }
    var isBuyerSelected by remember { mutableStateOf(false) }
    var isRetailerSelected by remember { mutableStateOf(false) }

    // Observe the buyer and company states
    val buyerState by userViewModel.buyerState.collectAsState()
    val companyState by userViewModel.companyState.collectAsState()
    val userType by userViewModel.userType.collectAsState()

    // If a buyer is logged in, navigate to the user home
    if (buyerState != null) {
        navActions.navigateToUserHome()
    }

    // If a company is logged in, navigate to the retailer home
    if (companyState != null) {
        navActions.navigateToRetailerHome() // Ensure you have this navigation route
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Login") })
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Buttons to choose user type
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Use collected userType for comparisons
                        val isBuyerSelected = userType == UserViewModel.UserType.BUYER
                        CustomButton(
                            onClick = {
                                userViewModel.setUserType(UserViewModel.UserType.BUYER)
                            },
                            text = "Buyer",
                            isActive = isBuyerSelected
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        val isRetailerSelected = userType == UserViewModel.UserType.COMPANY
                        CustomButton(
                            onClick = {
                                userViewModel.setUserType(UserViewModel.UserType.COMPANY)
                            },
                            text = "Retailer",
                            isActive = isRetailerSelected
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

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
            }
        }
    )
}


