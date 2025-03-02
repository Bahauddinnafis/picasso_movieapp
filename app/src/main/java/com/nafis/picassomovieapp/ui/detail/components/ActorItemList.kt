package com.nafis.picassomovieapp.ui.detail.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nafis.picassomovieapp.R
import com.nafis.picassomovieapp.movie_detail.domain.models.Cast
import com.nafis.picassomovieapp.ui.PicassoImage
import com.nafis.picassomovieapp.ui.detail.DetailViewModel

@Composable
fun ActorItemList(
    modifier: Modifier = Modifier,
    cast: Cast,
    imageUrl: String,
    detailViewModel: DetailViewModel = hiltViewModel()
) {
    Log.d("ActorItemList", "Loading image from URL: $imageUrl")
    var startTime by remember { mutableStateOf(0L) }
    val existingLoadingTime = detailViewModel.getLoadingTime(cast.firstName, imageUrl)

    LaunchedEffect(imageUrl) {
        startTime = 0L
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            PicassoImage(
                imageUrl = imageUrl,
                contentDescription = "${cast.firstName} ${cast.lastName}",
                modifier = modifier.size(100.dp),
                contentScale = ContentScale.Crop,
                onLoading = {
                   if (existingLoadingTime == null) {
                       Log.d("ImageURL", "Image URL: $imageUrl")
                       startTime = System.nanoTime()
                       Log.d("LoadingTime", "Start loading image for ${cast.firstName}")
                   }
                },
                onSuccess = { bitmap ->
                    if (startTime == 0L) {
                        Log.w("ActorItemList", "Loading time skipped (cached image?)")
                        return@PicassoImage
                    }
                    val endTime = System.nanoTime()
                    val loadingTime = (endTime - startTime) / 1_000_000

                    val runtime = Runtime.getRuntime()
                    val usedMemInMB = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024)

                    Log.d(
                        "LoadingTime",
                        "Picasso loading time for ${cast.firstName}: $loadingTime ms"
                    )
                    Log.d("MemoryUsage", "Memori setelah loading ${cast.firstName}: ${usedMemInMB} MB")
                    detailViewModel.addLoadingTime(cast.firstName, imageUrl, loadingTime)
                },
                onError = { error ->
                    if (startTime == 0L) {
                        Log.w("ActorItemList", "Error but startTime not set")
                        return@PicassoImage
                    }
                    val endTime = System.nanoTime()
                    val loadingTime = (endTime - startTime) / 1_000_000
                    Log.d(
                        "LoadingTime",
                        "Picasso loading failed for ${cast.firstName}: $loadingTime ms"
                    )
                    detailViewModel.addLoadingTime(cast.firstName, imageUrl, loadingTime)
                },
                placeholder = R.drawable.baseline_person_24
            )
        }
        // Gender Role (Actor / Actress)
        Text(text = cast.genderRole, style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = cast.firstName,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = cast.lastName,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )

    }
}