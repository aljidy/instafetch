package com.adam.instafetch.breedlist

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.adam.instafetch.BaseViewModel
import com.adam.instafetch.DogBreedModel
import com.adam.instafetch.DogsRepo
import kotlinx.coroutines.launch
import java.io.IOException

class BreedListViewModel(private val repo: DogsRepo) : BaseViewModel<DogBreedListState>(DogBreedListState()) {
    init {
        getBreeds()
    }

    private fun getBreeds() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            try {
                val breeds =
                    repo.getDogBreeds().sortedBy { it.breedId }
                setState { copy(breeds = breeds, isLoading = false) }
            } catch (e: IOException) {
                Log.e(this::class.simpleName, "Couldn't get breeds", e)

                setState { copy(isError = true, isLoading = false) }
            }
        }
    }
}


data class DogBreedListState(
    val breeds: List<DogBreedModel> = listOf(),
    val isError: Boolean = false,
    val isLoading: Boolean = false,
)
