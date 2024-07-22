package campus.tech.kakao.map.di

import campus.tech.kakao.map.data.repository.DefaultLocationRepository
import campus.tech.kakao.map.data.repository.DefaultPlaceRepository
import campus.tech.kakao.map.data.repository.DefaultSavedSearchWordRepository
import campus.tech.kakao.map.data.repository.LocationRepository
import campus.tech.kakao.map.data.repository.PlaceRepository
import campus.tech.kakao.map.data.repository.SavedSearchWordRepository
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
    abstract fun bindPlaceRepository(placeRepositoryImpl: DefaultPlaceRepository): PlaceRepository

    @Binds
    @ViewModelScoped
    abstract fun bindSavedSearchWordRepository(savedSearchWordRepositoryImpl: DefaultSavedSearchWordRepository): SavedSearchWordRepository

    @Binds
    @ViewModelScoped
    abstract fun bindLocationRepository(locationRepositoryImpl: DefaultLocationRepository): LocationRepository
}
