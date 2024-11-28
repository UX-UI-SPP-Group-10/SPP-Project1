package com.sppProject.app.view.components.buttons

import android.R.attr.contentDescription
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun BuyPageButton(onClick: () -> Unit, modifier: Modifier = Modifier){
    Button(
        onClick = onClick,
        modifier = modifier
            .width(45.dp) // Set button width
            .height(45.dp), // Set button height
        contentPadding = PaddingValues(0.dp) // Remove default padding
    ) {
        Icon(
            imageVector = Icons.Rounded.Home,
            contentDescription = "Price Icon",
            modifier = Modifier.size(80.dp) // Set icon size as desired
        )
    }
}