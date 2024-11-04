package com.sppProject.app.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sppProject.app.R
import com.sppProject.app.UserNavActions
import com.sppProject.app.data.data_class.Buyer
import com.sppProject.app.api_integration.fetchers.BuyerFetcher


sealed class CreatePageState(val content: @Composable () -> Unit) {
    class ShowRetailer : CreatePageState({ RetailerInfo() })
    class ShowUser : CreatePageState({ UserInfo() })
    object None : CreatePageState({
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Select an option above to get started.")
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePage(navActions: UserNavActions, buyerFetcher: BuyerFetcher) {
    var createPageState by remember { mutableStateOf<CreatePageState>(CreatePageState.None) }
    var sendInfo by remember { mutableStateOf(false) }
    var feedbackMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Create Profile") })
        },
        content = { innerPadding ->
            // Central container to display the selected composable based on the state

            // Buttons to switch between Retailer and User views
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
                    Button(
                        onClick = {
                            createPageState = CreatePageState.ShowRetailer()
                        }
                    ) {
                        Text("Retailer")
                    }
                    Spacer(modifier = Modifier.width(2.dp))
                    Button(
                        onClick = {
                            createPageState = CreatePageState.ShowUser()
                        }
                    ) {
                        Text("User")
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding) // Use innerPadding to avoid overlap with the top bar
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                createPageState.content()

                // Back button at the bottom left
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
    )




    if (sendInfo) {
        LaunchedEffect(sendInfo) {
            try {
                // Submit info based on the selected state
                if (createPageState is CreatePageState.ShowUser) {
                    buyerFetcher.createBuyer(Buyer(id = 0, name = "UserName")) // Example name
                    feedbackMessage = "User added successfully!"
                    navActions.navigateToLogin()
                } else if (createPageState is CreatePageState.ShowRetailer) {
                    buyerFetcher.createBuyer(Buyer(id = 0, name = "Retailer")) // Example name
                    feedbackMessage = "Retailer added successfully!"
                    navActions.navigateToLogin()
                }
            } catch (e: Exception) {
                feedbackMessage = e.message ?: "An error occurred while adding user."
            } finally {
                sendInfo = false // Reset sendInfo after submission
            }
        }
    }

    if (feedbackMessage.isNotEmpty()) {
        Text(feedbackMessage, modifier = Modifier.padding(16.dp))
    }
}


@Composable
fun RetailerInfo() {
    var company by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(top = 16.dp)) {
        InputField("Enter Company", company) { company = it }
        InputField("Enter Location", location) { location = it }
        InputField("Enter Password", password) { password = it }
        Button(onClick = { /* Handle create retailer */ }) {
            Text("Create Profile")
        }
    }
}

@Composable
fun UserInfo() {
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(top = 16.dp)) {
        InputField("Enter Username", name) { name = it }
        InputField("Enter Password", password) { password = it }
        Button(onClick = { /* Handle create user */ }) {
            Text("Create Profile")
        }
    }
}

@Composable
fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(16.dp))
}