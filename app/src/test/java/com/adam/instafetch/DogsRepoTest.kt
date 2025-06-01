package com.adam.instafetch

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class DogsRepoTest {
    private lateinit var dogsService: DogsService
    private lateinit var dogsRepo: DogsRepo

    @Before
    fun setUp() {
        dogsService = mock()
        dogsRepo = DogsRepoImpl(dogsService)
    }

    @Test
    fun `GIVEN dog breed with no breed type list WHEN getDogBreeds THEN map to DogBreedModel correctly`() =
        runTest {
            val response =
                ApiDogBreedResponse(
                    message =
                        hashMapOf(
                            "shiba" to listOf(),
                        ),
                )
            whenever(dogsService.getAllBreeds()).thenReturn(response)

            val result = dogsRepo.getDogBreeds()

            val expected =
                listOf(
                    DogBreedModel("shiba", "shiba", null),
                )

            assertEquals(expected, result)
        }

    @Test
    fun `GIVEN dog breed with breed type list WHEN getDogBreeds THEN map each DogBreedModel correctly`() =
        runTest {
            val response =
                ApiDogBreedResponse(
                    message =
                        hashMapOf(
                            "sheepdog" to listOf("english", "indian", "shetland"),
                        ),
                )
            whenever(dogsService.getAllBreeds()).thenReturn(response)

            val result = dogsRepo.getDogBreeds()

            val expected =
                listOf(
                    DogBreedModel("sheepdog/english", "sheepdog", "english"),
                    DogBreedModel("sheepdog/indian", "sheepdog", "indian"),
                    DogBreedModel("sheepdog/shetland", "sheepdog", "shetland"),
                )

            assertEquals(expected, result)
        }
}
