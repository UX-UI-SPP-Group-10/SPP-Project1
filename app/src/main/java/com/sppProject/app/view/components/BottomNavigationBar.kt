package com.sppProject.app.view.components

import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sppProject.app.UserNavActions
import com.sppProject.app.viewModel.UserViewModel

@Composable
fun BottomNavigationBar(
    navActions: UserNavActions,
    userViewModel: UserViewModel
) {
    val userType = userViewModel.userType.collectAsState()
    when (userType.value) {
        UserViewModel.UserType.BUYER -> BuyerBottomNavigationBar(navActions, userViewModel)
        UserViewModel.UserType.COMPANY -> RetailerBottomNavigationBar(navActions, userViewModel)
        null -> {
            Log.e("BottomNavigationBar", "User type is null. Failed loading Bottom Navigation Bar.")}
    }
}

@Composable
private fun BuyerBottomNavigationBar(
    navActions: UserNavActions,
    userViewModel: UserViewModel
) {
    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        Spacer(Modifier.width(16.dp))

        // Logout button
        LogoutButton(onClick = { userViewModel.logout() })

        Spacer(Modifier.width(120.dp))

        BuyPageButton(onClick = {
        })

        Spacer(Modifier.width(120.dp))


        ReciptButton(onClick = {

        })
    }
}

@Composable
private fun RetailerBottomNavigationBar(
    navActions: UserNavActions,
    userViewModel: UserViewModel
) {
    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        Spacer(Modifier.width(16.dp))

        // Logout button
        LogoutButton(onClick = { userViewModel.logout() })

        Spacer(Modifier.width(120.dp))

        CreateItemButton(onClick = {
            Log.d("RetailerBottomNavigationBar", "Create Item button clicked")
            navActions.navigateToCreateItem()
        })
    }
}
