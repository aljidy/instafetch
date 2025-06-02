package com.adam.instafetch.breedphotos

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import coil3.ColorImage
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.annotation.DelicateCoilApi
import coil3.test.FakeImageLoaderEngine
import com.adam.instafetch.theme.InstaFetchTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BreedPhotosScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private fun getFakeViewModel(state: BreedPhotosState) =
        object : BreedPhotosViewModel {
            override val state: StateFlow<BreedPhotosState> = MutableStateFlow(state)
        }

    @OptIn(DelicateCoilApi::class)
    @Before
    fun setup() {
        // Fakes the image loading for the AsyncImage, so we can assert images are displayed when an URL is returned
        val engine =
            FakeImageLoaderEngine.Builder()
                .intercept("https://example.com/image.jpg", ColorImage(Color.Red.toArgb(), width = 300, height = 300))
                .build()
        val imageLoader =
            ImageLoader.Builder(InstrumentationRegistry.getInstrumentation().targetContext) // I need context here
                .components { add(engine) }
                .build()
        SingletonImageLoader.setUnsafe(imageLoader)
    }

    @Test
    fun assertErrorMessageShownWhenDogRepoThrowsException() {
        val isErrorState = BreedPhotosState(isError = true, isLoading = false, dogPhotoUrl = listOf())

        composeTestRule.setContent {
            InstaFetchTheme {
                BreedPhotosScreen(
                    breedPhotosViewModel = getFakeViewModel(isErrorState),
                    breedName = "", // Not Under test
                ) { }
            }
        }

        composeTestRule.onNodeWithText("Something went wrong").assertIsDisplayed()
    }

    @Test
    fun assertLoadingSpinnerShownWhenRepoHasDelayInResponse() {
        val isLoadingState = BreedPhotosState(isError = false, isLoading = true, dogPhotoUrl = listOf())

        composeTestRule.setContent {
            InstaFetchTheme {
                BreedPhotosScreen(
                    breedPhotosViewModel = getFakeViewModel(isLoadingState),
                    breedName = "", // Not Under test
                ) { }
            }
        }

        composeTestRule.onNodeWithTag("CentredLoadingSpinner").assertIsDisplayed()
    }

    /**
     * Note: I've combined both the press and display assertions into a single test to minimise the runtime of the tests
     */
    @Test
    fun assertContentShownAndNavigationWorksWhenRepoHasValidResponse() {
        val isLoadingState = BreedPhotosState(isError = false, isLoading = false, dogPhotoUrl = List(9) { "https://example.com/image.jpg" })

        var clickedBack: Boolean? = null
        val testCallback: () -> Unit = {
            clickedBack = true
        }

        composeTestRule.setContent {
            InstaFetchTheme {
                BreedPhotosScreen(
                    breedPhotosViewModel = getFakeViewModel(isLoadingState),
                    breedName = "DogName",
                    onNavigateBackTapped = testCallback,
                )
            }
        }

        composeTestRule.onAllNodesWithTag("BreedPhotosScreen_Image").assertCountEquals(9)
        composeTestRule.onNodeWithText("DogName").assertExists("Top App Bar name not displayed")

        composeTestRule.onNodeWithTag("PhotoScreen_BackButton").performClick()
        assertEquals(true, clickedBack)
    }
}
