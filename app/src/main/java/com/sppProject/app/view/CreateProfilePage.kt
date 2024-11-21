package com.sppProject.app.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.sppProject.app.UserNavActions
import com.sppProject.app.api_integration.fetchers.BuyerFetcher
import com.sppProject.app.api_integration.fetchers.CompanyFetcher
import com.sppProject.app.data.UserSessionManager
import com.sppProject.app.view.components.BackButton
import com.sppProject.app.view.components.CustomButton
import com.sppProject.app.view.components.CustomToggleButton
import com.sppProject.app.viewModel.CreatePageViewModel


sealed class CreatePageState(val content: @Composable (CreatePageViewModel) -> Unit) {
    class ShowRetailer : CreatePageState({ viewModel -> RetailerInfo(viewModel) })
    class ShowUser : CreatePageState({ viewModel -> UserInfo(viewModel) })
    object None : CreatePageState({
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Select an option above to get started.")
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProfilePage(navActions: UserNavActions, buyerFetcher: BuyerFetcher, companyFetcher: CompanyFetcher, createPageViewModel: CreatePageViewModel) {
    val auth = FirebaseAuth.getInstance()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TopAppBarSetup(navActions)
                }
            )
        },
        content = {  padding ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 250.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val isUserActive = createPageViewModel.createPageState is CreatePageState.ShowUser
                    CustomToggleButton(
                        onClick = {
                            createPageViewModel.setCreatePageState(CreatePageState.ShowUser())
                        },
                        text = "User",
                        isActive = isUserActive
                    )
                    Spacer(modifier = Modifier.width(2.dp))

                    val isRetailerActive = createPageViewModel.createPageState is CreatePageState.ShowRetailer
                    CustomToggleButton(
                        onClick = {
                            createPageViewModel.setCreatePageState(CreatePageState.ShowRetailer())
                        },
                        text = "Retailer",
                        isActive = isRetailerActive
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Enter Email") },
                    modifier = Modifier.padding(8.dp)
                )

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Enter Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.padding(8.dp)
                )


                Box(
                    modifier = Modifier
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    createPageViewModel.createPageState.content(createPageViewModel)
                }

                CustomButton(
                    onClick = {
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val firebaseUser = auth.currentUser
                                    firebaseUser?.let {
                                        createPageViewModel.userSessionManager.saveFirebaseUserId(it.uid)
                                        createPageViewModel.sendInfo(navActions) // Sync additional data to Spring DB
                                    }
                                } else {
                                    // Handle errors
                                    createPageViewModel._feedbackMessage.value = "Account creation failed: ${task.exception?.message}"
                                }
                            }
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(horizontal = 32.dp),
                    text = "Create Profile"
                )
            }


        }
    )

    if (createPageViewModel.feedbackMessage.isNotEmpty()) {
        Text(createPageViewModel.feedbackMessage, modifier = Modifier.padding(16.dp))
    }
}

@Composable
private fun TopAppBarSetup(navActions: UserNavActions) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back button on the left
        BackButton(onClick = { navActions.navigateBack() })

        // Spacer with weight to push title to the center
        Spacer(modifier = Modifier.weight(1f))

        // Centered title
        Text(
            text = "Create Profile",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        )

        // Add a fixed-width spacer to balance the BackButton's space
        Spacer(modifier = Modifier.weight(1f))

        // Placeholder spacer equivalent to BackButton width
        Spacer(modifier = Modifier.width(50.dp)) // Adjust width to match BackButton's size
    }
}

@Composable
fun RetailerInfo(viewModel: CreatePageViewModel) {
    var company by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    Column(modifier = Modifier) {
        TextField(
            value = company,
            onValueChange = { company = it },
            label = { Text("Enter Company Name") },
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
        )
        TextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Enter Location") },
            modifier = Modifier.padding(8.dp)
        )
        viewModel.userName = company
    }
}

@Composable
fun UserInfo(viewModel: CreatePageViewModel) {
    var name by remember { mutableStateOf("") }

    Column(modifier = Modifier) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Enter Name") },
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
    viewModel.userName = name
}