package com.adam.instafetch.breedlist

import app.cash.turbine.test
import com.adam.instafetch.DogBreedModel
import com.adam.instafetch.DogsRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class BreedListViewModelImplTest {
    private lateinit var dogsRepo: DogsRepo
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        dogsRepo = mock()
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `WHEN ViewModel init THEN isLoading setTo true after initial false`() =
        runTest {
            whenever(dogsRepo.getDogBreeds()).thenReturn(emptyList())

            val viewmodel = BreedListViewModelImpl(dogsRepo, testDispatcher)

            viewmodel.state.test {
                val initialState = awaitItem()
                assertEquals(false, initialState.isLoading) // Initial value

                val loadingState = awaitItem()
                assertEquals(true, loadingState.isLoading)

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `GIVEN list of dog breeds returns from repo WHEN init viewModel THEN state set to sorted list and loading false`() =
        runTest {
            whenever(dogsRepo.getDogBreeds()).thenReturn(
                listOf(
                    DogBreedModel("shiba", "shiba"),
                    DogBreedModel("hound/walker", "hound", "walker"),
                ),
            )

            val viewmodel = BreedListViewModelImpl(dogsRepo, testDispatcher)

            advanceUntilIdle()

            assertEquals(
                viewmodel.state.value,
                DogBreedListState(
                    listOf(
                        DogBreedModel("hound/walker", "hound", "walker"),
                        DogBreedModel("shiba", "shiba"),
                    ),
                    isLoading = false,
                ),
            )
        }

    @Test
    fun `GIVEN getDogBreeds throws IOException WHEN init viewModel THEN state set to error and loading false`() =
        runTest {
            whenever(dogsRepo.getDogBreeds()).thenAnswer { throw IOException("test exception") }

            val viewmodel = BreedListViewModelImpl(dogsRepo, testDispatcher)

            advanceUntilIdle()

            assertEquals(
                DogBreedListState(isError = true, isLoading = false),
                viewmodel.state.value,
            )
        }
}
