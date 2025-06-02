package com.adam.instafetch

interface DogsRepo {
    /**
     * @return list dog breeds from DogApi
     */
    suspend fun getDogBreeds(): List<DogBreedModel>

    /**
     * @param breedId - DogApi\'s breed query param, e.g. akita or collie/border or hound/walker
     *
     * @return List of URLs of photos of dogs
     */
    suspend fun getBreedPhotos(breedId: String): List<String>
}

class DogsRepoImpl(
    private val dogsService: DogsService,
    private val dogsApiMapper: DogsApiMapper = DogsApiMapper
) : DogsRepo {
    companion object {
        private const val DEFAULT_PHOTO_NUMBER = 10
    }

    override suspend fun getDogBreeds(): List<DogBreedModel> {
        return dogsApiMapper.mapApiDogBreedResponse(dogsService.getAllBreeds())
    }

    override suspend fun getBreedPhotos(breedId: String): List<String> {
        return dogsApiMapper.mapApiDogPhotosResponse(
            dogsService.getPhotosOfBreeds(
                breedId,
                DEFAULT_PHOTO_NUMBER
            )
        )
    }
}
