package com.adam.instafetch

class DogsRepo(private val dogsService: DogsService) {
    companion object {
        private const val DEFAULT_PHOTO_NUMBER = 10
    }

    /**
     * Returns list dog breeds from DogApi
     */
    suspend fun getDogBreeds(): List<DogBreedModel> {
        return mapDogBreeds(dogsService.getAllBreeds())
    }

    /**
     * @param breedId - DogApi\'s breed query param, e.g. akita or collie/border or hound/walker
     *
     * Returns URLs of photos of dogs
     */
    suspend fun getBreedPhotos(breedId: String): ApiDogPhotosResponse {
        return dogsService.getPhotosOfBreeds(breedId, DEFAULT_PHOTO_NUMBER)
    }

    private fun mapDogBreeds(response: ApiDogBreedResponse): List<DogBreedModel> {
        return response.message.flatMap { (breedGroup, breedTypes) ->
            return@flatMap if (breedTypes.isEmpty()) {
                listOf(DogBreedModel(breedId = breedGroup, breedGroup = breedGroup))
            } else {
                breedTypes.map {
                    DogBreedModel(
                        breedId = "$breedGroup/$it",
                        breedGroup = breedGroup,
                        breedType = it,
                    )
                }
            }
        }
    }
}
