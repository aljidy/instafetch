package com.adam.instafetch

import kotlinx.serialization.Serializable
import java.util.Locale

/**
 * @param breedId DogApi\'s breed query param, e.g. akita or collie/border or hound/walker
 * @param breedGroup - either the breed name "akita" or the breed group like "hound" for Walker Hound or "bulldog" for English Bulldog
 * @param breedType - optional - if the breed is part of a breed group, this is line/type - e.g. "walker" for Walker Hound or "english" for English Bulldog
 */
@Serializable
data class DogBreedModel(
    val breedId: String,
    val breedGroup: String,
    val breedType: String? = null,
) {
    /**
     * Capitalise is deprecated in kotlin, including this here for ease
     */
    private fun String.capitalise(): String {
        return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }

    /**
     * Dog breed name for displaying to user - e.g. "German Shepard", "Shiba"
     */
    val userFriendlyName: String = "${breedType?.capitalise() ?: ""} ${breedGroup.capitalise()}".trim()
}
