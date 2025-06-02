package com.adam.instafetch

import android.content.Context
import com.adam.instafetch.DogsService.Companion.BASE_URL
import com.adam.instafetch.breedlist.BreedListViewModel
import com.adam.instafetch.breedlist.BreedListViewModelImpl
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

// Leaving context here in case it's need for injection/init in future
class DogsModule(applicationContext: Context) {
    private val dogsService: DogsService = createDogsService()
    val repo: DogsRepo = DogsRepoImpl(dogsService)

    companion object {
        private const val JSON_MIME_TYPE = "application/json; charset=UTF8"
    }

    fun injectBreedListViewModel(): BreedListViewModel = BreedListViewModelImpl(repo)

    private fun createOkHttpClient(): OkHttpClient {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    private fun createDogsService(): DogsService =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createOkHttpClient())
            .addConverterFactory(
                Json.asConverterFactory(
                    JSON_MIME_TYPE.toMediaType(),
                ),
            )
            .build()
            .create(DogsService::class.java)
}
