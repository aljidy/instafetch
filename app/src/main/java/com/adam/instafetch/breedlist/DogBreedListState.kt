package com.adam.instafetch.breedlist

import com.adam.instafetch.DogBreedModel

data class DogBreedListState(
    val breeds: List<DogBreedModel> = listOf(),
    val isError: Boolean = false,
    val isLoading: Boolean = false,
)