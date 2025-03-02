package com.nafis.picassomovieapp.ui.home.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddToQueue
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nafis.picassomovieapp.favorite.data.local.FavoriteListItem
import com.nafis.picassomovieapp.movie.domain.models.Movie
import com.nafis.picassomovieapp.ui.PicassoImage
import com.nafis.picassomovieapp.ui.favorite.FavoriteViewModel
import com.nafis.picassomovieapp.ui.home.HomeViewModel
import com.nafis.picassomovieapp.ui.home.itemSpacing
import com.nafis.picassomovieapp.ui.watchlist.WatchListViewModel
import com.nafis.picassomovieapp.watchlist.data.local.WatchListItem

@Composable
fun MovieCoverImageList(
    modifier: Modifier = Modifier,
    movie: Movie,
    onMovieClick: (id: Int) -> Unit,
    imageUrl: String,
    contentScale: ContentScale = ContentScale.Fit,
    watchListViewModel: WatchListViewModel = hiltViewModel(),
    favoriteViewModel: FavoriteViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState
) {
    Log.d("MovieCoverImageList", "Loading image from URL: $imageUrl")
    var startTime by remember { mutableStateOf(0L) }
    val existingLoadingTime = homeViewModel.getLoadingTime(movie.title, imageUrl)

    LaunchedEffect(imageUrl) {
        startTime = 0L
    }

    Box(
        modifier = modifier
            .size(width = 200.dp, height = 300.dp)
            .padding(itemSpacing)
            .clickable { onMovieClick(movie.id) },
    ){
        PicassoImage(
            imageUrl = imageUrl,
            contentDescription = "${movie.title}_${movie.id}",
            modifier = modifier
                .size(width = 200.dp, height = 300.dp)
                .padding(itemSpacing)
                .clickable { onMovieClick(movie.id) },
            contentScale = contentScale,
            onLoading = {
               if (existingLoadingTime == null) {
                   Log.d("ImageURL", "Image URL: $imageUrl")
                   startTime = System.nanoTime()
                   Log.d("LoadingTime", "Start loading image for ${movie.title}")
               }
            },
            onSuccess = { bitmap ->
                if (startTime == 0L) {
                    Log.w("MovieCoverImageList", "Loading time skipped (cached image?)")
                    return@PicassoImage
                }
                val endTime = System.nanoTime()
                val loadingTime = (endTime - startTime) / 1_000_000

                val runtime = Runtime.getRuntime()
                val usedMemInMB = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024)

                Log.d("LoadingTime", "Picasso loading time for ${movie.title}: $loadingTime ms")
                Log.d("MemoryUsage", "Memori setelah loading ${movie.title}: ${usedMemInMB} MB")
                homeViewModel.addLoadingTime(movie.title, imageUrl, loadingTime)
            },
            onError = { error ->
                if (startTime == 0L) {
                    Log.w("MovieCoverImageList", "Error but startTime not set")
                    return@PicassoImage
                }
                val endTime = System.nanoTime()
                val loadingTime = (endTime - startTime) / 1_000_000
                Log.d("LoadingTime", "Picasso loading failed for ${movie.title}: $loadingTime ms")
                homeViewModel.addLoadingTime(movie.title, imageUrl, loadingTime)
            }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MovieCard(
                shapes = CircleShape,
                modifier = Modifier
                    .padding(4.dp)
                    .testTag("Favorite_${movie.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorite_${movie.id}",
                    modifier = Modifier
                        .padding(4.dp)
                        .clickable {
                            favoriteViewModel.addToFavorite(
                                FavoriteListItem(
                                    id = movie.id,
                                    title = movie.title,
                                    posterPath = movie.posterPath
                                )
                            )
                        }
                )
            }
            MovieCard(
                shapes = CircleShape,
                modifier = Modifier
                    .padding(4.dp)
                    .testTag("Watchlist_${movie.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.AddToQueue,
                    contentDescription = "Watchlist_${movie.id}",
                    modifier = Modifier
                        .padding(4.dp)
                        .clickable {
                            watchListViewModel.addToWatchlist(
                                WatchListItem(
                                    id = movie.id,
                                    title = movie.title,
                                    posterPath = movie.posterPath
                                )
                            )
                        }
                )
            }
        }
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            color = Color.Black.copy(.8f),
            contentColor = Color.White,
            shape = RoundedCornerShape(
                bottomEnd = 30.dp,
                bottomStart = 30.dp
            )
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = movie.title,
                    maxLines = 1
                )
            }
        }

    }
}