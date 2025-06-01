package com.adam.instafetch

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.adam.instafetch.breedlist.BreedListScreen
import com.adam.instafetch.breedlist.BreedListViewModel
import com.adam.instafetch.theme.InstaFetchTheme
import com.adam.instafetch.util.fakeDogRepo
import kotlinx.coroutines.delay
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


private val dogBreed1 = DogBreedModel("breed1", "Breed 1")
private val dogBreed2 = DogBreedModel("breed2", "Breed", "type 2")

@RunWith(AndroidJUnit4::class)
class BreedListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun assertErrorMessageShownWhenDogRepoThrowsException() {
        val viewModel = BreedListViewModel(
            fakeDogRepo(getDogBreeds = {
                throw IOException("Test Exception ")
            })
        )

        composeTestRule.setContent {
            InstaFetchTheme {
                BreedListScreen(
                    viewModel = viewModel
                ) { } // Not Under test
            }
        }

        composeTestRule.onNodeWithText("Something went wrong").assertIsDisplayed()
    }

    @Test
    fun assertLoadingSpinnerShownWhenRepoHasDelayInResponse() {
        val viewModel = BreedListViewModel(
            fakeDogRepo(getDogBreeds = {
                delay(3000)
                listOf(dogBreed1, dogBreed2)
            })
        )

        composeTestRule.setContent {
            InstaFetchTheme {
                BreedListScreen(
                    viewModel = viewModel,
                    {  } // Not Under test
                )
            }
        }

        composeTestRule.onNodeWithTag("CentredLoadingSpinner").assertIsDisplayed()
    }

    /**
     * Note: I've combined both the press and display assertions into a single test to minimise the runtime of the tests
     */
    @Test
    fun assertContentShownAndClickCallsCallbackWhenRepoHasDelayInResponse() {
        val viewModel = BreedListViewModel(
            fakeDogRepo(getDogBreeds = {
                listOf(dogBreed1, dogBreed2)
            })
        )

        // Not using Mockito (see README) so creating our own verification mechanism here
        var clickedBreed: DogBreedModel? = null
        val testCallback: (DogBreedModel) -> Unit = { breed ->
            clickedBreed = breed
        }

        composeTestRule.setContent {
            InstaFetchTheme {
                BreedListScreen(
                    viewModel = viewModel,
                    testCallback
                )
            }
        }

        composeTestRule.onNodeWithText("Breed 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Breed 1").performClick()
        assertEquals(dogBreed1, clickedBreed)

        composeTestRule.onNodeWithText("Type 2 Breed").assertIsDisplayed()
        composeTestRule.onNodeWithText("Type 2 Breed").performClick()
        assertEquals(dogBreed2, clickedBreed)
    }
}
