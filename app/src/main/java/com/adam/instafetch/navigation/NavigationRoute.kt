package com.adam.instafetch.navigation

import kotlinx.serialization.Serializable

sealed class NavigationRoute {
    @Serializable
    data object BreedList : NavigationRoute()

    @Serializable
    data class BreedPhotos(val breedId: String, val breedName: String) : NavigationRoute() {
        companion object {
            const val BREED_ID_KEY = "breedId"
            const val BREED_NAME_KEY = "breedName"
        }
    }
}
