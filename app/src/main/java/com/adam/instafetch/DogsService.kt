package com.adam.instafetch

import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Path

interface DogsService {
    companion object {
        const val BASE_URL = "https://dog.ceo/api/"
    }

    /**
     * Returns list of all dog breeds
     */
    @GET("breeds/list/all")
    suspend fun getAllBreeds(): ApiDogBreedResponse

    /**
     * Returns list of photo URls for a dog breed
     *
     * @param breedName - breed name returned via [getAllBreeds]  separated by slash if 2 part
     * e.g. hound/walker, akita, encoded set to true so that Retrofit doesn't URL encode the / to %2F
     * @param numberOfPhotos - number of photos urls for the API return
     */
    @GET("breed/{breed}/images/random/{numberOfPhotos}")
    suspend fun getPhotosOfBreeds(
        @Path("breed", encoded = true) breedName: String,
        @Path("numberOfPhotos") numberOfPhotos: Int,
    ): ApiDogPhotosResponse
}

interface Response<T> {
    val message: T
    val status: String
}

@Serializable
data class ApiDogBreedResponse(
    override val message: HashMap<String, List<String>> = hashMapOf(),
    override val status: String = "", // Defaulting as we don't want to crash if this doesn't come back
) : Response<HashMap<String, List<String>>>

@Serializable
data class ApiDogPhotosResponse(
    override val message: List<String> = listOf(),
    override val status: String = "", // Defaulting as we don't want to crash if this doesn't come back
) : Response<List<String>>
