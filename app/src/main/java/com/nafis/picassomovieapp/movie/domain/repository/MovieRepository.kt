package com.nafis.picassomovieapp.movie.domain.repository

import com.nafis.picassomovieapp.movie.domain.models.Movie
import com.nafis.picassomovieapp.utils.Response
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun fetchDiscoverMovie(): Flow<Response<List<Movie>>>
    fun fetchTrendingMovie(): Flow<Response<List<Movie>>>

}