package com.sppProject.app.view.components

import androidx.compose.runtime.Composable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview


// can do it with a boolean instead
@Override
@Composable
fun CustomToggleButton(onClick: () -> Unit, text: String, isActive: Boolean) {
    Button(
        onClick = onClick, // Call the passed in onClick function
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isActive) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurface // Change color based on active state
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (isActive) 20.dp else 4.dp, // Adjust elevation based on active state
            pressedElevation = 8.dp // Elevation when the button is pressed
        ),
        shape = RoundedCornerShape(16.dp) // Button shape
    ) {
        Text(text = text, color = Color.White) // Button text
    }
}

@Preview(showBackground = true)
@Composable
fun CustomButtonTogglePreview() {
    CustomToggleButton(onClick = { /* Handle button click */ }, text = "Click Me", isActive = true)
}

