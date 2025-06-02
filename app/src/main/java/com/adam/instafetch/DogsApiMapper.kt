package com.adam.instafetch

object DogsApiMapper {
    fun mapApiDogBreedResponse(response: ApiDogBreedResponse): List<DogBreedModel> =
        response.message.flatMap { (breedGroup, breedTypes) ->
            if (breedTypes.isEmpty()) {
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

    fun mapApiDogPhotosResponse(response: ApiDogPhotosResponse): List<String> = response.message
}