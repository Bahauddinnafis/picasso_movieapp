package com.nafis.picassomovieapp.ui.detail.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nafis.picassomovieapp.movie_detail.domain.models.MovieDetail
import com.nafis.picassomovieapp.ui.PicassoImage
import com.nafis.picassomovieapp.ui.detail.DetailViewModel
import com.nafis.picassomovieapp.ui.home.components.MovieCard
import com.nafis.picassomovieapp.ui.home.defaultPadding
import com.nafis.picassomovieapp.ui.home.itemSpacing
import com.nafis.picassomovieapp.ui.theme.primaryLightHighContrast
import com.nafis.picassomovieapp.utils.K

@Composable
fun DetailTopContent(
    modifier: Modifier = Modifier,
    movieDetail: MovieDetail,
    detailViewModel: DetailViewModel = hiltViewModel()
) {
    var startTime by remember { mutableStateOf(0L) }
    val imageUrl = "${K.BASE_IMAGE_URL}${movieDetail.posterPath}"
    val existingLoadingTime = detailViewModel.getLoadingTime(movieDetail.title, imageUrl)

    LaunchedEffect(imageUrl) {
        startTime = 0L
    }

    Box(modifier = modifier.fillMaxWidth()) {
        PicassoImage(
            imageUrl = "${K.BASE_IMAGE_URL}${movieDetail.posterPath}",
            contentDescription = movieDetail.title,
            modifier = modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop,
            onLoading = {
                if (existingLoadingTime == null) {
                    Log.d("ImageURL", "Image URL: $imageUrl")
                    startTime = System.nanoTime()
                    Log.d("LoadingTime", "Start loading image for ${movieDetail.title}")
                }
            },
            onSuccess = { bitmap ->
                if (startTime == 0L) {
                    Log.w("DetailTopContent", "Loading time skipped (cached image?)")
                    return@PicassoImage
                }
                val endTime = System.nanoTime()
                val loadingTime = (endTime - startTime) / 1_000_000

                val runtime = Runtime.getRuntime()
                val usedMemInMB = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024)

                Log.d("LoadingTime", "Picasso loading time for ${movieDetail.title}: $loadingTime ms")
                Log.d("MemoryUsage", "Memori setelah loading ${movieDetail.title}: ${usedMemInMB} MB")
                detailViewModel.addLoadingTime(movieDetail.title, imageUrl, loadingTime)
            },
            onError = { error ->
                if (startTime == 0L) {
                    Log.w("Detail Top Content", "Error but startTime not set")
                    return@PicassoImage
                }
                val endTime = System.nanoTime()
                val loadingTime = (endTime - startTime) / 1_000_000
                Log.d("LoadingTime", "Picasso loading failed for ${movieDetail.title}: $loadingTime ms")
                detailViewModel.addLoadingTime(movieDetail.title, imageUrl, loadingTime)
            }
        )
        MovieDetailComponent(
            rating = movieDetail.voteAverage,
            releaseDate = movieDetail.releaseDate,
            modifier = Modifier
                .align(Alignment.BottomStart)
        )
    }

}

@Composable
private fun MovieDetailComponent(
    modifier: Modifier = Modifier,
    rating: Double,
    releaseDate: String
) {
    Column(modifier) {
        MovieCard(
            modifier = Modifier.padding(horizontal = defaultPadding)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row (
                    modifier = Modifier.padding(4.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Rating",
                        tint = Color.Yellow,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = rating.toString())
                }
                Spacer(modifier = Modifier.width(itemSpacing))
                VerticalDivider(modifier = Modifier.height(16.dp))
                Spacer(modifier = Modifier.width(itemSpacing))
                Text(
                    text = releaseDate,
                    modifier = Modifier.padding(6.dp),
                    maxLines = 1,
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = defaultPadding)
            ) {
                Card(
                    onClick = {},
                    modifier = Modifier.weight(1f), // Half 2 items
                    shape = RoundedCornerShape(topStart = 30.dp, bottomStart = 30.dp),
                ) {
                    Row(
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = "play")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Watch Now")
                    }
                }
                Card(
                    onClick = {},
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                        contentColor = primaryLightHighContrast
                    ),
                    modifier = Modifier.weight(1f), // Half 2 items
                    shape = RoundedCornerShape(topEnd = 30.dp, bottomEnd = 30.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.Movie, contentDescription = "trailer")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Watch Trailer")
                    }
                }
            }
        }
    }
}