package com.sppProject.app.view.components

import android.R.attr.name
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    borderColor: Color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
    labelText: String = "",
    value: String,
    onValueChange: (String) -> Unit,
    shape: Shape = RoundedCornerShape(12.dp),
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = labelText,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface // Customizing label text color
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = borderColor, // Border color when focused
            unfocusedBorderColor = borderColor, // Border color when unfocused
            focusedContainerColor = Color.Transparent, // Container color when focused
            unfocusedContainerColor = Color.Transparent, // Container color when unfocused
            cursorColor = Color.Black
        ),
        shape = shape,
        singleLine = true,
        visualTransformation = visualTransformation,
        modifier = modifier
            .padding(4.dp) // Padding to give some space around the text field
    )
}
