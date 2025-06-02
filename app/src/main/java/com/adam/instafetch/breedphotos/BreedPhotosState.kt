package com.adam.instafetch.breedphotos

data class BreedPhotosState(
    val dogPhotoUrl: List<String> = listOf(),
    val isError: Boolean = false,
    val isLoading: Boolean = false,
)