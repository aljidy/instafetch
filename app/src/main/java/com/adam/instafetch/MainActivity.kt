package com.adam.instafetch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.adam.instafetch.breedlist.BreedListScreen
import com.adam.instafetch.breedlist.BreedListViewModel
import com.adam.instafetch.breedphotos.BreedPhotosScreen
import com.adam.instafetch.navigation.NavigationRoute
import com.adam.instafetch.theme.InstaFetchTheme

class MainActivity : ComponentActivity() {
    private val breedListViewModel: BreedListViewModel by lazy {
        DogsApp.from(applicationContext).injectBreedListViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            InstaFetchTheme {
                NavHost(navController = navController, startDestination = NavigationRoute.BreedList) {
                    composable<NavigationRoute.BreedList> {
                        BreedListScreen(breedListViewModel) { breed ->
                            navController.navigate(route = NavigationRoute.BreedPhotos(breed.breedId, breed.userFriendlyName))
                        }
                    }

                    composable<NavigationRoute.BreedPhotos> { backStackEntry ->
                        val breedPhotos: NavigationRoute.BreedPhotos = backStackEntry.toRoute()
                        BreedPhotosScreen(
                            breedId = breedPhotos.breedId,
                            breedName = breedPhotos.breedName,
                            onNavigateBackTapped = { navController.popBackStack() },
                        )
                    }
                }
            }
        }
    }
}

