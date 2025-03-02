package com.nafis.picassomovieapp.di

import android.content.Context
import com.nafis.picassomovieapp.favorite.data.local.FavoriteDatabase
import com.nafis.picassomovieapp.favorite.data.repository.FavoriteRepository
import com.nafis.picassomovieapp.ui.favorite.FavoriteViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FavoriteModule {

    @Provides
    @Singleton
    fun provideFavoriteDatabase(@ApplicationContext context: Context): FavoriteDatabase {
        return FavoriteDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideFavoriteRepository(database: FavoriteDatabase): FavoriteRepository {
        return FavoriteRepository(database.favoriteDao())
    }
}

@Module
@InstallIn(ViewModelComponent::class)
object FavoriteViewModelModule {
    @Provides
    fun provideFavoriteViewModel(repository: FavoriteRepository): FavoriteViewModel {
        return FavoriteViewModel(repository)
    }
}