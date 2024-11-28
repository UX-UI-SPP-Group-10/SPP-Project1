package com.sppProject.app.view

import android.util.Log
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.sppProject.app.UserNavActions
import com.sppProject.app.view.components.buttons.CustomButton
import com.sppProject.app.view.components.CustomTextField
import com.sppProject.app.view.components.buttons.CustomToggleButton
import com.sppProject.app.viewModel.UserViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(
    userViewModel: UserViewModel,
    navActions: UserNavActions
) {
    val auth = FirebaseAuth.getInstance()
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val buyerState by userViewModel.buyerState.collectAsState()
    val companyState by userViewModel.companyState.collectAsState()
    val userType by userViewModel.userType.collectAsState()
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(buyerState, companyState) {
        Log.d("LoginPage", "Checking login states in LaunchedEffect")
        when {
            buyerState != null -> {
                Log.d("LoginPage", "Navigating to User Home for buyer: ${buyerState}")
                navActions.navigateFromLoginToUserHome()
            }
            companyState != null -> {
                Log.d("LoginPage", "Navigating to Retailer Home for company: ${companyState}")
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
                    password = password,
                    onNameChange = { name = it }, // Update name state here
                    onPasswordChange = { password = it }, // Update password state here
                    onCreateProfileClick = { navActions.navigateToCreateProfile() },
                    onUserTypeSelect = { userViewModel.setUserType(it) },
                    onLoginClick = {
                        if (name.isBlank() || password.isBlank()) {
                            errorMessage = "Email and password must not be empty."
                        } else {
                            auth.signInWithEmailAndPassword(name, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val firebaseUser = auth.currentUser
                                        firebaseUser?.let {
                                            userViewModel.fetchUserProfile(
                                                onSuccess = { userType ->
                                                    when (userType) {
                                                        UserViewModel.UserType.BUYER -> {
                                                            navActions.navigateFromLoginToUserHome()
                                                        }

                                                        UserViewModel.UserType.COMPANY -> {
                                                            navActions.navigateFromLoginToRetailerHome()
                                                        }

                                                        null -> {
                                                            errorMessage = "Unknown user type."
                                                            auth.signOut()
                                                        }
                                                    }
                                                },
                                                onFailure = {
                                                    errorMessage = "Login Failed: user not found "
                                                    auth.signOut()
                                                }
                                            )
                                        }
                                    }else {
                                        errorMessage = "Login Failed: Email or password is incorrect."
                                    }
                                }
                        }
                    }
                )


                // Show error message if any
                errorMessage?.let { error ->
                    Spacer(modifier = Modifier.height(36.dp)) // Moved down by 20.dp (16.dp + 20.dp)
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

    )
}

@Composable
private fun LoginContent(
    userType: UserViewModel.UserType,
    name: String,
    password: String,
    onNameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onCreateProfileClick: () -> Unit,
    onUserTypeSelect: (UserViewModel.UserType) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        /*UserTypeSelector(userType = userType, onUserTypeSelect = onUserTypeSelect)

        Spacer(modifier = Modifier.height(32.dp))
        */
        CustomTextField(value = name, labelText = "Email", onValueChange = onNameChange)

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(value = password, labelText = "Password", onValueChange = onPasswordChange, visualTransformation = PasswordVisualTransformation())

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
/*
@Composable
private fun UsernameInputField(name: String, onNameChange: (String) -> Unit) {
    TextField(
        value = name,
        onValueChange = onNameChange, // This should update the name state in LoginPage
        label = { Text("Enter email") }
    )
}
*/
