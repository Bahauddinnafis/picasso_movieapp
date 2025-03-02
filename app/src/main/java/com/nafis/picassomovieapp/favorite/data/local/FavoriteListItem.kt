package com.nafis.picassomovieapp.favorite.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class FavoriteListItem (
    @PrimaryKey
    val id: Int,

    val title: String,
    val posterPath: String
)