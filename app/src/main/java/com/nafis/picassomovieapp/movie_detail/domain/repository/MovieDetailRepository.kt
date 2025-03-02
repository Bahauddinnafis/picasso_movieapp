package com.nafis.picassomovieapp.movie_detail.domain.repository

import com.nafis.picassomovieapp.movie.domain.models.Movie
import com.nafis.picassomovieapp.movie_detail.domain.models.MovieDetail
import com.nafis.picassomovieapp.utils.Response
import kotlinx.coroutines.flow.Flow

interface MovieDetailRepository {
    fun fetchMovieDetail(movieId: Int): Flow<Response<MovieDetail>>
    fun fetchMovie(): Flow<Response<List<Movie>>>
}