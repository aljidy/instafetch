package com.adam.instafetch

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class DogsApiMapperTest {
    private lateinit var dogsService: DogsService
    private val dogsApiMapper: DogsApiMapper = DogsApiMapper

    @Before
    fun setUp() {
        dogsService = mock()
    }

    @Test
    fun `GIVEN dog breed with no breed type list WHEN mapApiDogBreedResponse THEN map to DogBreedModel correctly`() =
        runTest {

            val result = dogsApiMapper.mapApiDogBreedResponse(
                ApiDogBreedResponse(
                    message =
                    hashMapOf(
                        "shiba" to listOf(),
                    ),
                )
            )

            val expected =
                listOf(
                    DogBreedModel("shiba", "shiba", null),
                )

            assertEquals(expected, result)
        }

    @Test
    fun `GIVEN dog breed with breed type list WHEN mapApiDogBreedResponse THEN map each DogBreedModel correctly`() =
        runTest {
            val result = dogsApiMapper.mapApiDogBreedResponse(
                ApiDogBreedResponse(
                    message =
                    hashMapOf(
                        "sheepdog" to listOf("english", "indian", "shetland"),
                    ),
                )
            )

            val expected =
                listOf(
                    DogBreedModel("sheepdog/english", "sheepdog", "english"),
                    DogBreedModel("sheepdog/indian", "sheepdog", "indian"),
                    DogBreedModel("sheepdog/shetland", "sheepdog", "shetland"),
                )

            assertEquals(expected, result)
        }


    @Test
    fun `GIVEN ApiDogPhotosResponse WHEN mapApiDogPhotosResponse THEN map to list of URLs correctly`() = runTest {
        val result = dogsApiMapper.mapApiDogPhotosResponse(
            ApiDogPhotosResponse(
                message =
                listOf(
                    "https://example.com/shiba.png"
                ),
            )
        )


        assertEquals(listOf(
            "https://example.com/shiba.png"
        ), result)
    }
}