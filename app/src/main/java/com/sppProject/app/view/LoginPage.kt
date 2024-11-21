package com.sppProject.app.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.sppProject.app.view.components.CustomToggleButton
import com.sppProject.app.viewModel.UserViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(
    userViewModel: UserViewModel,
    navActions: UserNavActions
) {
    var name by remember { mutableStateOf("") }
    val buyerState by userViewModel.buyerState.collectAsState()
    val companyState by userViewModel.companyState.collectAsState()
    val userType by userViewModel.userType.collectAsState()

    // Navigate based on the logged-in state
    LaunchedEffect(buyerState, companyState) {
        when {
            buyerState != null -> {
                navActions.navigateFromLoginToUserHome()
            }
            companyState != null -> {
                navActions.navigateFromLoginToRetailerHome()
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
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
                LoginContent(
                    userType = userType ?: UserViewModel.UserType.BUYER, // Default to BUYER
                    name = name,
                    onNameChange = { name = it }, // Update name state here
                    onLoginClick = {
                        userViewModel.login(name)
                        val destination = if (userType == UserViewModel.UserType.COMPANY) {
                            navActions.navigateFromLoginToRetailerHome()
                        } else {
                            navActions.navigateFromLoginToUserHome()
                        }
                    },
                    onCreateProfileClick = { navActions.navigateToCreateProfile() },
                    onUserTypeSelect = { userViewModel.setUserType(it) }
                )
            }
        }
    )
}


@Composable
private fun LoginContent(
    userType: UserViewModel.UserType,
    name: String,
    onNameChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onCreateProfileClick: () -> Unit,
    onUserTypeSelect: (UserViewModel.UserType) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        UserTypeSelector(userType = userType, onUserTypeSelect = onUserTypeSelect)

        Spacer(modifier = Modifier.height(32.dp))

        UsernameInputField(name = name, onNameChange = onNameChange)

        Spacer(modifier = Modifier.height(32.dp))

        CustomButton(text = "Log in", onClick = onLoginClick)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Or",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomButton(text = "Create Profile", onClick = onCreateProfileClick)
    }
}

@Composable
private fun UserTypeSelector(
    userType: UserViewModel.UserType,
    onUserTypeSelect: (UserViewModel.UserType) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        CustomToggleButton(
            onClick = { onUserTypeSelect(UserViewModel.UserType.BUYER) },
            text = "Buyer",
            isActive = userType == UserViewModel.UserType.BUYER
        )
        Spacer(modifier = Modifier.width(16.dp))
        CustomToggleButton(
            onClick = { onUserTypeSelect(UserViewModel.UserType.COMPANY) },
            text = "Retailer",
            isActive = userType == UserViewModel.UserType.COMPANY
        )
    }
}

@Composable
private fun UsernameInputField(name: String, onNameChange: (String) -> Unit) {
    TextField(
        value = name,
        onValueChange = onNameChange, // This should update the name state in LoginPage
        label = { Text("Enter Username") }
    )
}
