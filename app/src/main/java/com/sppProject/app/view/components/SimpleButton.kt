package com.sppProject.app.view.components

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CustomButton(
    onClick: () -> Unit,
    text: String,
    isActive: Boolean = true, // Indicate if the button is active
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = isActive,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
            contentColor = if (isActive) Color.White else Color.Black // Change text color based on active state
        )
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CustomButtonPreview() {
    CustomButton(onClick = { /* Handle button click */ }, text = "Button Text")
}
