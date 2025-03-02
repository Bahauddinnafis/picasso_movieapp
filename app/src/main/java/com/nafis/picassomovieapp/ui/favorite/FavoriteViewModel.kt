package com.nafis.picassomovieapp.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nafis.picassomovieapp.favorite.data.local.FavoriteListItem
import com.nafis.picassomovieapp.favorite.data.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val repository: FavoriteRepository
): ViewModel() {

    private val _favorite = MutableStateFlow<List<FavoriteListItem>>(emptyList())
    val favorite: StateFlow<List<FavoriteListItem>> get() = _favorite

    private val _snakbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> get() = _snakbarMessage

    fun addToFavorite(movie: FavoriteListItem) {
        viewModelScope.launch {
            repository.addToFavorite(movie)
            _snakbarMessage.value = "Berhasil menambahkan ke favorite: ${movie.title}"
            loadFavorite()
        }
    }

    fun loadFavorite() {
        viewModelScope.launch {
            repository.getFavorite().collect { list ->
                _favorite.value = list
            }
        }
    }

    fun removeFromFavorite(id: Int) {
        viewModelScope.launch {
            repository.removeFromFavorite(id)
            loadFavorite()
        }
    }

    fun clearSnackbarMessage() {
        _snakbarMessage.value = null
    }

}