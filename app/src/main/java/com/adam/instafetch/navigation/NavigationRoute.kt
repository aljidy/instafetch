package com.adam.instafetch.navigation

import kotlinx.serialization.Serializable

sealed class NavigationRoute {
    @Serializable
    object BreedList

    @Serializable
    data class BreedPhotos(val breedId: String, val breedName: String)
}
