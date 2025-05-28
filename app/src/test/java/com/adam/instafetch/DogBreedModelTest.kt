package com.adam.instafetch

import org.junit.Assert.assertEquals
import org.junit.Test

class DogBreedModelTest {

    @Test
    fun `GIVEN dogBreed with only breedGroup WHEN get userFriendlyName THEN return captialise breedGroup`()  {
        val dogBreed =
            DogBreedModel(
                breedId = "akita",
                breedGroup = "akita",
            )

        assertEquals("Akita", dogBreed.userFriendlyName)
    }

    @Test
    fun `GIVEN dogBreed with breedGroup and Type WHEN get userFriendlyName THEN return captialised breedType and breedGroup `()  {
        val dogBreed =
            DogBreedModel(
                breedId = "hound/walker",
                breedGroup = "hound",
                breedType = "walker",
            )

        assertEquals("Walker Hound", dogBreed.userFriendlyName)
    }
}
