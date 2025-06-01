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
import com.adam.instafetch.ApiDogPhotosResponse
import com.adam.instafetch.theme.InstaFetchTheme
import com.adam.instafetch.util.fakeDogRepo
import kotlinx.coroutines.delay
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class BreedPhotosScreenKtTest {
    @get:Rule
    val composeTestRule = createComposeRule()

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
        val viewModel =
            BreedPhotosViewModel(
                fakeDogRepo(
                    getBreedPhotos = {
                        throw IOException("Test Exception ")
                    },
                ),
                breedId = "",
            )

        composeTestRule.setContent {
            InstaFetchTheme {
                BreedPhotosScreen(
                    breedPhotosViewModel = viewModel,
                    breedId = "", // Not Under test
                    breedName = "", // Not Under test
                ) { }
            }
        }

        composeTestRule.onNodeWithText("Something went wrong").assertIsDisplayed()
    }

    @Test
    fun assertLoadingSpinnerShownWhenRepoHasDelayInResponse() {
        val viewModel =
            BreedPhotosViewModel(
                fakeDogRepo(getBreedPhotos = {
                    delay(1000) // TODO explain race condition
                    ApiDogPhotosResponse()
                }),
                breedId = "",
            )

        composeTestRule.setContent {
            InstaFetchTheme {
                BreedPhotosScreen(
                    breedPhotosViewModel = viewModel,
                    breedId = "", // Not Under test
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
        val viewModel =
            BreedPhotosViewModel(
                fakeDogRepo(getBreedPhotos = {
                    ApiDogPhotosResponse(
                        List(9) { "https://example.com/image.jpg" },
                    )
                }),
                breedId = "",
            )

        var clickedBack: Boolean? = null
        val testCallback: () -> Unit = {
            clickedBack = true
        }

        composeTestRule.setContent {
            InstaFetchTheme {
                BreedPhotosScreen(
                    breedPhotosViewModel = viewModel,
                    breedId = "", // Not Under test
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
