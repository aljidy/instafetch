package com.adam.instafetch.breedphotos

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.adam.instafetch.R
import com.adam.instafetch.breedphotos.BreedPhotosViewModelImpl.Companion.extrasWithBreedId
import com.adam.instafetch.theme.InstaFetchTheme
import com.adam.instafetch.ui.CentredLoadingSpinner
import com.adam.instafetch.ui.GenericErrorMessage

const val BREED_LIST_GRID_CELL_SIZE_DP = 200

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedPhotosScreen(
    breedId: String,
    breedName: String,
    breedPhotosViewModel: BreedPhotosViewModel =
        viewModel(
            modelClass = BreedPhotosViewModelImpl::class,
            factory = BreedPhotosViewModelImpl.Factory,
            extras = extrasWithBreedId(breedId),
        ),
    onNavigateBackTapped: () -> Unit,
) {
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(
            title = {
                Text(text = breedName)
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBackTapped, modifier = Modifier.testTag("PhotoScreen_BackButton")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.photo_screen_back_button_content_description),
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            },
        )
    }) { innerPadding ->
        val state by breedPhotosViewModel.state.collectAsState()

        when {
            state.isError -> {
                GenericErrorMessage(Modifier.fillMaxSize())
            }

            state.isLoading -> {
                CentredLoadingSpinner(Modifier.fillMaxSize())
            }

            else -> {
                PhotoGrid(
                    Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    state,
                )
            }
        }
    }
}

@Composable
private fun PhotoGrid(
    modifier: Modifier = Modifier,
    state: BreedPhotoState,
) {
    LazyVerticalStaggeredGrid(
        modifier = modifier,
        columns = StaggeredGridCells.Adaptive(BREED_LIST_GRID_CELL_SIZE_DP.dp),
        content = {
            items(
                state.dogPhotoUrl,
            ) { url ->
                AsyncImage(
                    model = url,
                    modifier = Modifier.width(BREED_LIST_GRID_CELL_SIZE_DP.dp).testTag("BreedPhotosScreen_Image"),
                    contentScale = ContentScale.Fit,
                    contentDescription = null,
                )
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun DetailScreenPreview() {
    InstaFetchTheme {
        PhotoGrid(
            state =
                BreedPhotoState(
                    listOf(
                        "https://images.dog.ceo/breeds/australian-kelpie/IMG_2599.jpg",
                        "https://images.dog.ceo/breeds/australian-kelpie/IMG_3675.jpg",
                        "https://images.dog.ceo/breeds/australian-kelpie/IMG_4918.jpg",
                        "https://images.dog.ceo/breeds/australian-kelpie/IMG_7387.jpg",
                        "https://images.dog.ceo/breeds/australian-kelpie/Resized_20200214_191118_346649120350209.jpg",
                        "https://images.dog.ceo/breeds/australian-kelpie/Resized_20200303_233358_108952253645051.jpg",
                        "https://images.dog.ceo/breeds/australian-kelpie/Resized_20200416_142905_108884348190285.jpg",
                        "https://images.dog.ceo/breeds/australian-kelpie/Resized_20201114_133404_109264920155921.jpg",
                    ),
                ),
        )
    }
}
