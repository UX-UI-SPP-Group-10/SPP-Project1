package com.sppProject.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sppProject.app.api_integration.RetrofitClient
import com.sppProject.app.api_integration.fetchers.BuyerFetcher
import com.sppProject.app.api_integration.data_class.Buyer
import com.sppProject.app.api_integration.api_service.BuyerApiService
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var buyerFetcher: BuyerFetcher // Use BuyerFetcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize BuyerFetcher with the created ApiService
        buyerFetcher = BuyerFetcher(RetrofitClient.createApiService(BuyerApiService::class.java))

        // Set the content of the activity
        setContent {
            Logindpage(buyerFetcher)
        }
    }
}

@Composable
fun Logindpage(buyerFetcher: BuyerFetcher) {
    var logOn by remember { mutableStateOf(false) }
    var newUser by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    if (!newUser) {
        Column(modifier = Modifier.padding(top = 16.dp)) {
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Enter Uesrname") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Enter Password") },
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { newUser = true }) {
                Text("Creat profile")
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Button(
                    onClick = {
                        logOn = true
                    },
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Text("Log in")
                }
            }

        }
    }
    else{
        App(backToLogin = { newUser = false })
    }
}

@Composable
fun App(backToLogin: () -> Unit){
    var retylerInfomation by remember { mutableStateOf(false) }
    var youserInfomation by remember { mutableStateOf(false) }
    var sendInfo by remember { mutableStateOf(false) }
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 32.dp))
    {
        RetylerInfo(
            retylerInfomation = retylerInfomation,
            onClick = {
                retylerInfomation = !retylerInfomation
                if(retylerInfomation) youserInfomation = false},
            sendInfo = sendInfo
        )
        Spacer(modifier = Modifier.height(16.dp))
        UserInfo(
            youserInfomation = youserInfomation,
            onClick = {
                youserInfomation = !youserInfomation
                if (youserInfomation) retylerInfomation = false
            },
            sendInfo = sendInfo
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp) // Optional padding from the edges
    ) {
        Button(
            onClick = {
                sendInfo = true
            },
            modifier = Modifier.align(Alignment.BottomEnd) // Aligns the button to the bottom-right
        ) {
            Text("Make Profile")
        }

        Button(onClick = backToLogin,
            modifier = Modifier.align(Alignment.BottomStart)){
            Text("Back")
        }
    }
}

@Composable
fun RetylerInfo(
    retylerInfomation: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    sendInfo: Boolean){
    var company by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Button(onClick = onClick) {
        Text("Retailer")
    }

    if(retylerInfomation){
        Column(modifier = Modifier.padding(top = 16.dp)){
            TextField(
                value = company,
                onValueChange = { company = it },
                label = { Text("Enter Company") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Enter Location") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Enter Password") },
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
    if(retylerInfomation && sendInfo){
        //send infomation and gow to logind page
    }
}

@Composable
fun UserInfo(
    youserInfomation: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    sendInfo: Boolean)
{
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Button(onClick = onClick) {
        Text("User")
    }
    if(youserInfomation){
        Column(modifier = Modifier.padding(top = 16.dp)){
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Enter Uesrname") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Enter Password") },
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if(youserInfomation && sendInfo){
        //send infomation and gow to logind page
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleBuyerApp(buyerFetcher: BuyerFetcher) {
    var buyers by remember { mutableStateOf(listOf<Buyer>()) }
    var name by remember { mutableStateOf("") }
    var feedbackMessage by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Name Input Field
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Enter Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Submit Button
        Button(onClick = {
            if (name.isNotBlank()) {
                coroutineScope.launch {
                    try {
                        val newBuyer = buyerFetcher.createBuyer(Buyer(id = 0, name = name)) // Create buyer
                        buyers = buyers + newBuyer
                        name = "" // Clear name input after successful submission
                        feedbackMessage = "Buyer added successfully!"
                    } catch (e: Exception) {
                        feedbackMessage = e.message ?: "An error occurred while adding buyer."
                    }
                }
            } else {
                feedbackMessage = "Name cannot be empty."
            }
        }) {
            Text("Submit Name")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Fetch Buyers Button
        Button(onClick = {
            coroutineScope.launch {
                try {
                    buyers = buyerFetcher.fetchBuyers() // Fetch buyers
                    feedbackMessage = if (buyers.isNotEmpty()) "Buyers fetched successfully!" else "No buyers found."
                } catch (e: Exception) {
                    feedbackMessage = e.message ?: "An error occurred while fetching buyers."
                }
            }
        }) {
            Text("Fetch Buyers")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display feedback message
        if (feedbackMessage.isNotBlank()) {
            Text(feedbackMessage, color = if (feedbackMessage.contains("error", true)) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Display buyers list
        Text("Buyers List:")
        buyers.forEach { buyer ->
            Text("ID: ${buyer.id}, Name: ${buyer.name}")
        }
    }
}
