package com.nafis.picassomovieapp.ui

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import com.nafis.picassomovieapp.R

@Composable
fun PicassoImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    placeholder: Int = R.drawable.bg_image_movie, // Placeholder default
    onLoading: () -> Unit = {},
    onSuccess: (Bitmap) -> Unit = {}, // Terima Bitmap sebagai parameter
    onError: (Exception) -> Unit = {} // Terima Exception sebagai parameter
) {
    val context = LocalContext.current
    val bitmapState = remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(imageUrl) {
        Picasso.get()
            .load(imageUrl)
            .placeholder(placeholder)
            .error(placeholder)
            .into(object : Target {
                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    bitmap?.let {
                        bitmapState.value = it
                        onSuccess(it)

                        Log.d(
                            "PicassoImage",
                            "Gambar dimuat: ${bitmap.width}x${bitmap.height}, Memori: ${bitmap.allocationByteCount / 1024} KB"
                        )
                    }
                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                    e?.let {
                        onError(it)
                    }
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                    onLoading()
                }
            })
    }

    bitmapState.value?.let { bitmap ->
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = contentDescription,
            modifier = modifier.fillMaxSize(),
            contentScale = contentScale
        )
    } ?: Image(
        painter = painterResource(id = placeholder),
        contentDescription = contentDescription,
        modifier = modifier.fillMaxSize(),
        contentScale = contentScale
    )
}