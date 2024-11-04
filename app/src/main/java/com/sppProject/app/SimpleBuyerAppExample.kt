import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sppProject.app.data.data_class.Buyer
import com.sppProject.app.api_integration.fetchers.BuyerFetcher
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleBuyerApp(buyerFetcher: BuyerFetcher) {
    var buyers by remember { mutableStateOf(listOf<Buyer>()) }
    var name by remember { mutableStateOf("") }
    var feedbackMessage by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Name Input Field
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Enter Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Submit Button
        Button(onClick = {
            if (name.isNotBlank()) {
                coroutineScope.launch {
                    try {
                        val newBuyer = buyerFetcher.createBuyer(Buyer(id = 0, name = name)) // Create buyer
                        buyers = buyers + newBuyer
                        name = "" // Clear name input after successful submission
                        feedbackMessage = "Buyer added successfully!"
                    } catch (e: Exception) {
                        feedbackMessage = e.message ?: "An error occurred while adding buyer."
                    }
                }
            } else {
                feedbackMessage = "Name cannot be empty."
            }
        }) {
            Text("Submit Name")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Fetch Buyers Button
        Button(onClick = {
            coroutineScope.launch {
                try {
                    buyers = buyerFetcher.fetchBuyers() // Fetch buyers
                    feedbackMessage = if (buyers.isNotEmpty()) "Buyers fetched successfully!" else "No buyers found."
                } catch (e: Exception) {
                    feedbackMessage = e.message ?: "An error occurred while fetching buyers."
                }
            }
        }) {
            Text("Fetch Buyers")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display feedback message
        if (feedbackMessage.isNotBlank()) {
            Text(feedbackMessage, color = if (feedbackMessage.contains("error", true)) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Display buyers list
        Text("Buyers List:")
        buyers.forEach { buyer ->
            Text("ID: ${buyer.id}, Name: ${buyer.name}")
        }
    }
}