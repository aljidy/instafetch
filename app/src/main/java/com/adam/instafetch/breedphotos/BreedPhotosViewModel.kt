package com.adam.instafetch.breedphotos

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.adam.instafetch.BaseViewModel
import com.adam.instafetch.DogsApp
import com.adam.instafetch.DogsRepoImpl
import kotlinx.coroutines.launch
import java.io.IOException

data class BreedPhotoState(
    val dogPhotoUrl: List<String> = listOf(),
    val isError: Boolean = false,
    val isLoading: Boolean = false,
)

class BreedPhotosViewModel(private val repo: DogsRepoImpl, private val breedId: String) :
    BaseViewModel<BreedPhotoState>(BreedPhotoState()) {
    init {
        getPhotos()
    }

    private fun getPhotos() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }

            try {
                val photoUrl = repo.getBreedPhotos(breedId).message

                setState { copy(dogPhotoUrl = photoUrl, isLoading = false) }
            } catch (e: IOException) {
                Log.e(this::class.simpleName, "Couldn't get breed photos", e)

                setState { copy(isError = true, isLoading = false) }
            }
        }
    }

    companion object {
        private val BREED_ID = object : CreationExtras.Key<String> {}

        val Factory: ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val application = checkNotNull(this[APPLICATION_KEY])
                    val dogsModule = DogsApp.from(application.applicationContext)

                    val breedId = this[BREED_ID] as String

                    BreedPhotosViewModel(
                        dogsModule.repo,
                        breedId,
                    )
                }
            }

        @Composable
        fun extrasWithBreedId(breedName: String): MutableCreationExtras {
            return MutableCreationExtras(
                ((LocalViewModelStoreOwner.current as HasDefaultViewModelProviderFactory).defaultViewModelCreationExtras),
            ).apply {
                set(BREED_ID, breedName)
            }
        }
    }
}
