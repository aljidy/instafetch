package com.adam.instafetch.util

import com.adam.instafetch.ApiDogPhotosResponse
import com.adam.instafetch.DogBreedModel
import com.adam.instafetch.DogsRepo

//TODO should this be called factory or in an object
fun fakeDogRepo(
    getDogBreeds: suspend () -> List<DogBreedModel> = {TODO("Not Under Test")},
    getBreedPhotos: suspend (String) -> ApiDogPhotosResponse = {TODO("Not Under Test")},
) = object : DogsRepo{
    override suspend fun getDogBreeds(): List<DogBreedModel> = getDogBreeds()

    override suspend fun getBreedPhotos(breedId: String) = getBreedPhotos(breedId)

}