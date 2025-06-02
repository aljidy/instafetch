package com.adam.instafetch.breedlist

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.adam.instafetch.DogBreedModel
import com.adam.instafetch.theme.InstaFetchTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private val dogBreed1 = DogBreedModel("breed1", "Breed 1")
private val dogBreed2 = DogBreedModel("breed2", "Breed", "type 2")

@RunWith(AndroidJUnit4::class)
class BreedListScreenTest {
    private fun getFakeViewModel(state: DogBreedListState) =
        object : BreedListViewModel {
            override val state: StateFlow<DogBreedListState> = MutableStateFlow(state)
        }

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun assertErrorMessageShownWhenIsErrorState() {
        val errorState = DogBreedListState(isError = true, isLoading = false, breeds = listOf())

        composeTestRule.setContent {
            InstaFetchTheme {
                BreedListScreen(
                    viewModel = getFakeViewModel(errorState),
                ) { } // Not Under test
            }
        }

        composeTestRule.onNodeWithText("Something went wrong").assertIsDisplayed()
    }

    @Test
    fun assertLoadingSpinnerShownWhenIsLoadingState() {
        val isLoadingState =
            DogBreedListState(
                isError = false,
                isLoading = true,
                breeds = listOf(),
            )

        composeTestRule.setContent {
            InstaFetchTheme {
                BreedListScreen(
                    viewModel = getFakeViewModel(isLoadingState),
                    { }, // Not Under test
                )
            }
        }

        composeTestRule.onNodeWithTag("CentredLoadingSpinner").assertIsDisplayed()
    }

    /**
     * Note: I've combined both the press and display assertions into a single test to minimise the runtime of the tests
     */
    @Test
    fun assertContentShownAndClickCallsCallbackWhenDogBreedsStateSet() {
        // Not using Mockito (see README) so creating our own verification mechanism here
        var clickedBreed: DogBreedModel? = null
        val testCallback: (DogBreedModel) -> Unit = { breed ->
            clickedBreed = breed
        }

        val breedsState =
            DogBreedListState(
                isError = false,
                isLoading = false,
                breeds = listOf(dogBreed1, dogBreed2),
            )

        composeTestRule.setContent {
            InstaFetchTheme {
                BreedListScreen(
                    viewModel = getFakeViewModel(breedsState),
                    testCallback,
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
// TODO test navigation
