package campus.tech.kakao.map.di

import campus.tech.kakao.map.data.repository.PlaceRepository
import campus.tech.kakao.map.data.repository.PlaceRepositoryImpl
import campus.tech.kakao.map.data.repository.SavedSearchWordRepository
import campus.tech.kakao.map.data.repository.SavedSearchWordRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {
    @Binds
    @ViewModelScoped
    abstract fun bindPlaceRepository(placeRepositoryImpl: PlaceRepositoryImpl): PlaceRepository

    @Binds
    @ViewModelScoped
    abstract fun bindSavedSearchWordRepository(savedSearchWordRepositoryImpl: SavedSearchWordRepositoryImpl): SavedSearchWordRepository
}
