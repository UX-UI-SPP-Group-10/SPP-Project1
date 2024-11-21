package com.sppProject.app.view.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CustomButton(
    onClick: () -> Unit,
    text: String,
    isActive: Boolean = true, // Indicate if the button is active
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(50), // Rounded corners
    borderWidth: Dp = 1.dp,
    borderColor: Color = MaterialTheme.colorScheme.onBackground,
) {
    ElevatedButton(
        onClick = onClick,
        enabled = isActive,
        shape = shape,
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = if (isActive) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
            contentColor = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        ),
        border = BorderStroke(borderWidth, borderColor), // Adding a border directly to ElevatedButton
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 6.dp, // Default elevation for the button
            pressedElevation = 10.dp  // Elevation when pressed
        ),
        modifier = modifier
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            style = MaterialTheme.typography.bodyLarge,
            color = if (isActive) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CustomButtonPreview() {
    CustomButton(onClick = { /* Handle button click */ }, text = "Button Text")
}
