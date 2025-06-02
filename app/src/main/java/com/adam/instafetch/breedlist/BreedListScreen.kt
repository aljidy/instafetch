package com.adam.instafetch.breedlist

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.adam.instafetch.DogBreedModel
import com.adam.instafetch.R
import com.adam.instafetch.theme.InstaFetchTheme
import com.adam.instafetch.theme.Spacing
import com.adam.instafetch.ui.CentredLoadingSpinner
import com.adam.instafetch.ui.GenericErrorMessage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedListScreen(
    viewModel: BreedListViewModel,
    onNextButtonTap: (DogBreedModel) -> Unit,
) {
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(
            title = {
                Text(text = stringResource(R.string.app_name))
            },
        )
    }) { innerPadding ->
        val state by viewModel.state.collectAsState()

        when {
            state.isError -> {
                GenericErrorMessage(Modifier.fillMaxSize())
            }

            state.isLoading -> {
                CentredLoadingSpinner(Modifier.fillMaxSize())
            }

            else -> {
                BreedLazyList(
                    Modifier
                        .padding(innerPadding)
                        .padding(Spacing.xxl)
                        .fillMaxSize(),
                    state,
                    onNextButtonTap,
                )
            }
        }
    }
}

@Composable
private fun BreedLazyList(
    modifier: Modifier = Modifier,
    state: DogBreedListState,
    onNextButtonTap: (DogBreedModel) -> Unit,
) {
    LazyColumn(modifier = modifier) {
        items(state.breeds) { breed ->
            Column {
                Surface(
                    modifier =
                        Modifier
                            .padding(bottom = Spacing.xl)
                            .fillMaxWidth()
                            .clickable { onNextButtonTap(breed) },
                    tonalElevation = 1.dp,
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Row(
                        modifier =
                            Modifier
                                .padding(horizontal = Spacing.xl, vertical = Spacing.l)
                                .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = breed.userFriendlyName,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            // Decorative, content description provided by text
                            contentDescription = null,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BreedListScreenPreview() {
    InstaFetchTheme {
        val context = LocalContext.current

        BreedListScreen(viewModel<BreedListViewModelImpl>()) {
            Toast.makeText(context, "Button tapped", Toast.LENGTH_LONG).show()
        }
    }
}
