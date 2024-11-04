import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ActivityScenario
import com.sppProject.app.MainActivity
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import org.junit.Rule

class LoginSteps {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Given("I launch the MainActivity")
    fun i_launch_the_MainActivity() {
        // Launch MainActivity
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Then("I should see the login page")
    fun i_should_see_the_login_page() {
        // Check if the page has the "Enter Username" text field, which indicates LoginPage is displayed
        composeTestRule
            .onNodeWithText("Enter Username")
            .assertIsDisplayed()
    }

    @Then("I should see the username and password fields and login button")
    fun i_should_see_username_password_and_login_button() {
        // Check that the username field is displayed by searching for its label text
        composeTestRule
            .onNodeWithText("Enter Username")
            .assertIsDisplayed()

        // Check that the password field is displayed by searching for its label text
        composeTestRule
            .onNodeWithText("Enter Password")
            .assertIsDisplayed()

        // Check that the "Log in" button is displayed by searching for its button text
        composeTestRule
            .onNodeWithText("Log in")
            .assertIsDisplayed()

        // Optionally, you can also check for the "Create profile" button
        composeTestRule
            .onNodeWithText("Create profile")
            .assertIsDisplayed()
    }
}
