package com.sppProject.app.view.components.buttons

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sppProject.app.R

@Composable
fun LogoutButton(onClick: () -> Unit, modifier: Modifier = Modifier){
    Button(
        onClick = onClick,
        modifier = modifier
            .width(45.dp) // Set button width
            .height(45.dp), // Set button height
        contentPadding = PaddingValues(0.dp) // Remove default padding
    ) {
        Icon(
            painter = painterResource(id = R.drawable.logout),
            contentDescription = "Logout",
            modifier = Modifier
                .size(80.dp)
                .scale(-1f)
        )
    }
}