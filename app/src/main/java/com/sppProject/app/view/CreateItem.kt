package com.sppProject.app.view

import android.R.attr.description
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.sppProject.app.UserNavActions
import com.sppProject.app.model.api_integration.fetchers.ItemFetcher
import com.sppProject.app.model.data.UserSessionManager
import com.sppProject.app.model.data.data_class.Company
import com.sppProject.app.model.data.data_class.Item
import com.sppProject.app.view.components.CustomTextField
import com.sppProject.app.view.components.buttons.BackButton
import com.sppProject.app.view.components.buttons.CustomButton
import com.sppProject.app.viewModel.ItemViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ItemPage(itemViewModel: ItemViewModel) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var numberOfItem by remember { mutableStateOf("0") }
    var price by remember { mutableStateOf("0.0") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(64.dp))

        CustomTextField(
            value = title,
            onValueChange = { title = it },
            labelText = "Title",
            modifier = Modifier
        )

        Spacer(modifier = Modifier.height(16.dp))

        //ImagePicker(modifier = Modifier.height(300.dp))

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(
            value = numberOfItem,
            onValueChange = { input ->
                // Allow only digits
                if (input.all { it.isDigit() }) {
                    numberOfItem = input
                }
            },
            labelText = "Number of items",
            modifier = Modifier
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(
            value = price,
            onValueChange = { input ->
                // Allow only digits and one decimal point
                if (input.matches(Regex("^[0-9]*\\.?[0-9]*"))) {
                    price = input
                }
            },
            labelText = "Item price",
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(
            value = description,
            onValueChange = { description = it },
            labelText = "Item description",
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        CustomButton(
            onClick = {
                itemViewModel.onTitleChange(title)
                itemViewModel.onNumberOfItemChange(numberOfItem)
                itemViewModel.onDescriptionChange(description)
                itemViewModel.onPriceChange(price)
                itemViewModel.postItem()
            },
            text = "Post Item",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .width(200.dp)
        )

        BackButton(
            onClick = {itemViewModel.userNavActions.navigateBack()},
            modifier = Modifier.align(Alignment.TopStart)
        )
    }
}

@Composable
fun ImagePicker(modifier: Modifier) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    val context = LocalContext.current

    // Activity launcher for selecting an image
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bmp = BitmapFactory.decodeStream(inputStream)
            bitmap = bmp?.asImageBitmap()
        }
    }

    Box(
        modifier = modifier,  // Apply the passed modifier here
        contentAlignment = Alignment.BottomEnd // Align content to the bottom-end
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()  // Fill the available space in the Box
        ) {

            // Display the selected image with a fixed height
            bitmap?.let {
                Image(
                    bitmap = it,
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(8.dp)
                )
            }
        }

        // Box at the bottom-right corner within ImagePicker space
        Box(
            modifier = Modifier
                .size(50.dp)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = { launcher.launch("image/*") }) {
                Text("Pick an Image")
            }
        }
    }
}
