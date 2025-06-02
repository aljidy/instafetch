package com.adam.instafetch.breedphotos

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.adam.instafetch.DogsRepo
import com.adam.instafetch.navigation.NavigationRoute.BreedPhotos.Companion.BREED_ID_KEY
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
class BreedPhotosViewModelImplTest {

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
            whenever(dogsRepo.getBreedPhotos("")).thenReturn(listOf())

            val viewmodel =
                BreedPhotosViewModelImpl(
                    dogsRepo,
                    SavedStateHandle(
                        mapOf(BREED_ID_KEY to "") // Not under test but required
                    )
                )

            viewmodel.state.test {
                val initialState = awaitItem()
                assertEquals(false, initialState.isLoading) // Initial value

                val loadingState = awaitItem()
                assertEquals(true, loadingState.isLoading)

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `GIVEN list of dog photos returns from repo WHEN init viewModel THEN state set to list and loading false`() =
        runTest {
            whenever(dogsRepo.getBreedPhotos("shiba")).thenReturn(
                listOf("https://example.com/shiba.png"),
            )

            val viewmodel = BreedPhotosViewModelImpl(
                dogsRepo, SavedStateHandle(
                    mapOf(
                        BREED_ID_KEY to "shiba"
                    )
                )
            )

            advanceUntilIdle()

            assertEquals(
                viewmodel.state.value,
                BreedPhotosState(
                    listOf("https://example.com/shiba.png"),
                    isLoading = false,
                ),
            )
        }

    @Test
    fun `GIVEN getBreedPhotos throws IOException WHEN init viewModel THEN state set to error and loading false`() =
        runTest {
            whenever(dogsRepo.getBreedPhotos("testBreed")).thenAnswer { throw IOException("test exception") }

            val viewmodel = BreedPhotosViewModelImpl(
                dogsRepo,
                SavedStateHandle(mapOf(BREED_ID_KEY to "testBreed"))
            )

            advanceUntilIdle()

            assertEquals(
                BreedPhotosState(isError = true, isLoading = false),
                viewmodel.state.value,
            )
        }
}
