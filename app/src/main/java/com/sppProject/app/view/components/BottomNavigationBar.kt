package com.sppProject.app.view.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sppProject.app.UserNavActions
import com.sppProject.app.viewModel.UserViewModel

@Composable
fun BottomNavigationBar(viewModel: UserViewModel, navActions: UserNavActions) {
    BottomAppBar(Modifier.fillMaxWidth(),
        content = {
            Spacer(Modifier.width(16.dp))

            LogoutButton(onClick = { viewModel.logout() })

            Spacer(Modifier.width(120.dp))

            BuyPageButton(onClick = {})

            Spacer(Modifier.width(120.dp))

            ReciptButton(onClick = {}) //TODO

        },
        containerColor = MaterialTheme.colorScheme.primary
    )
}