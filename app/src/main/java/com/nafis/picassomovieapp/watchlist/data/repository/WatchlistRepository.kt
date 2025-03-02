package com.nafis.picassomovieapp.watchlist.data.repository

import com.nafis.picassomovieapp.watchlist.data.local.WatchListDao
import com.nafis.picassomovieapp.watchlist.data.local.WatchListItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WatchListRepository @Inject constructor(
    private val watchListDao: WatchListDao
) {
    suspend fun addToWatchList(movie: WatchListItem) {
        watchListDao.addToWatchList(movie)
    }

    fun getWatchlist(): Flow<List<WatchListItem>> {
        return watchListDao.getWatchList()
    }

    suspend fun removeFromWatchlist(id: Int) {
        watchListDao.removeFromWatchList(id)
    }
}