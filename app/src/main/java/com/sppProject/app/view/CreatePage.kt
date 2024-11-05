package com.sppProject.app.view

import androidx.compose.foundation.layout.Arrangement
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
import com.sppProject.app.api_integration.fetchers.CompanyFetcher
import com.sppProject.app.view.components.CustomButton
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
fun CreatePage(navActions: UserNavActions, buyerFetcher: BuyerFetcher, companyFetcher: CompanyFetcher) {
    val viewModel: CreatePageViewModel = remember { CreatePageViewModel(buyerFetcher, companyFetcher) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Create Profile") })
        },
        content = { innerPadding ->
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
                    val isUserActive = viewModel.createPageState is CreatePageState.ShowUser
                    CustomButton(
                        onClick = {
                            viewModel.setCreatePageState(CreatePageState.ShowUser())
                        },
                        text = "User",
                        isActive = isUserActive
                    )
                    Spacer(modifier = Modifier.width(2.dp))

                    val isRetailerActive = viewModel.createPageState is CreatePageState.ShowRetailer
                    CustomButton(
                        onClick = {
                            viewModel.setCreatePageState(CreatePageState.ShowRetailer())
                        },
                        text = "Retailer",
                        isActive = isRetailerActive
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                viewModel.createPageState.content(viewModel)
                Button(onClick = {
                    viewModel.sendInfo(navActions)
                },
                    modifier = Modifier.align(Alignment.BottomEnd)) {
                    Text("Create Profile")
                }

                Button(onClick = {
                        navActions.navigateBack()
                },
                    modifier = Modifier.align(Alignment.BottomStart)
                ) {
                    Text("Back")
                }
            }
        }
    )

    if (viewModel.feedbackMessage.isNotEmpty()) {
        Text(viewModel.feedbackMessage, modifier = Modifier.padding(16.dp))
    }
}



@Composable
fun RetailerInfo(viewModel: CreatePageViewModel) {
    var company by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(top = 16.dp)) {
        InputField("Enter Company", company) { company = it }
        InputField("Enter Location", location) { location = it }
        InputField("Enter Password", password) { password = it }
    }
    viewModel.userName = company
}

@Composable
fun UserInfo(viewModel: CreatePageViewModel) {
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(top = 16.dp)) {
        InputField("Enter Username", name) { name = it }
        InputField("Enter Password", password) { password = it }
    }
    viewModel.userName = name
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