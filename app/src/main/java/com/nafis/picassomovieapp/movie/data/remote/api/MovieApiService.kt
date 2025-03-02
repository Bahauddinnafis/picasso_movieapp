package com.nafis.picassomovieapp.movie.data.remote.api

import com.nafis.picassomovieapp.BuildConfig
import com.nafis.picassomovieapp.movie.data.remote.models.MovieDto
import com.nafis.picassomovieapp.utils.K
import retrofit2.http.GET
import retrofit2.http.Query


interface MovieApiService {

    @GET(K.MOVIE_ENDPOINT)
    suspend fun fetchDiscoverMovie(
        @Query("api_key") apiKey: String = BuildConfig.apiKey,
        @Query("include_adult") includeAdult: Boolean = false
    ): MovieDto

    @GET(K.TRENDING_MOVIE_ENDPOINT)
    suspend fun fetchTrendingMovie(
        @Query("api_key") apiKey: String = BuildConfig.apiKey,
        @Query("include_adult") includeAdult: Boolean = false
    ): MovieDto

}