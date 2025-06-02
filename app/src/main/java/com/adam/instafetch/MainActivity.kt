package com.adam.instafetch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.adam.instafetch.breedlist.BreedListScreen
import com.adam.instafetch.breedphotos.BreedPhotosScreen
import com.adam.instafetch.navigation.NavigationRoute
import com.adam.instafetch.theme.InstaFetchTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            InstaFetchTheme {
                NavHost(
                    navController = navController,
                    startDestination = NavigationRoute.BreedList
                ) {
                    composable<NavigationRoute.BreedList> {
                        BreedListScreen { breed ->
                            navController.navigate(NavigationRoute.BreedPhotos(breed.breedId, breed.userFriendlyName)) {
                            }
                        }
                    }

                    composable<NavigationRoute.BreedPhotos> { backStackEntry ->
                        checkNotNull(backStackEntry.arguments?.getString(NavigationRoute.BreedPhotos.BREED_ID_KEY))
                        val breedName = checkNotNull(backStackEntry.arguments?.getString(NavigationRoute.BreedPhotos.BREED_NAME_KEY))
                        
                        BreedPhotosScreen(
                             breedName = breedName,
                            onNavigateBackTapped = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
