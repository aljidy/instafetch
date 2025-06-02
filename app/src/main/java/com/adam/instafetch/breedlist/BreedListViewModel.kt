package com.adam.instafetch.breedlist

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.adam.instafetch.BaseViewModel
import com.adam.instafetch.DogsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

interface BreedListViewModel {
    val state: StateFlow<DogBreedListState>
}

@HiltViewModel
class BreedListViewModelImpl @Inject constructor(
    private val repo: DogsRepo,
      private val dispatcher: CoroutineDispatcher
) : BaseViewModel<DogBreedListState>(DogBreedListState()), BreedListViewModel {
    init {
        getBreeds()
    }

    private fun getBreeds() {
        viewModelScope.launch(dispatcher) {
            setState { copy(isLoading = true) }
            try {
                val breeds = repo.getDogBreeds().sortedBy { it.breedId }
                setState { copy(breeds = breeds, isLoading = false) }
            } catch (e: IOException) {
                Log.e(this::class.simpleName, "Couldn't get breeds", e)
                setState { copy(isError = true, isLoading = false) }
            }
        }
    }
}


