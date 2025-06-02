package com.adam.instafetch.breedphotos

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.adam.instafetch.BaseViewModel
import com.adam.instafetch.DogsRepo
import com.adam.instafetch.navigation.NavigationRoute.BreedPhotos.Companion.BREED_ID_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

interface BreedPhotosViewModel {
    val state: StateFlow<BreedPhotosState>
}

@HiltViewModel
class BreedPhotosViewModelImpl @Inject constructor(
    private val repo: DogsRepo,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<BreedPhotosState>(BreedPhotosState()), BreedPhotosViewModel {

    private val breedId: String = checkNotNull(savedStateHandle[BREED_ID_KEY]) { "$BREED_ID_KEY parameter is missing" }

    init {
        getPhotos()
    }

    private fun getPhotos() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }

            try {
                val photoUrl = repo.getBreedPhotos(breedId)

                setState { copy(dogPhotoUrl = photoUrl, isLoading = false) }
            } catch (e: IOException) {
                Log.e(this::class.simpleName, "Couldn't get breed photos", e)

                setState { copy(isError = true, isLoading = false) }
            }
        }
    }
}
