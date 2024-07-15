package campus.tech.kakao.map.di

import android.content.Context
import campus.tech.kakao.map.data.SavedSearchWordDBHelper
import campus.tech.kakao.map.data.network.service.KakaoLocalService
import campus.tech.kakao.map.data.repository.PlaceRepository
import campus.tech.kakao.map.data.repository.PlaceRepositoryImpl
import campus.tech.kakao.map.data.repository.SavedSearchWordRepository
import campus.tech.kakao.map.data.repository.SavedSearchWordRepositoryImpl
import campus.tech.kakao.map.ui.ViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideSavedSearchWordDBHelper(
        @ApplicationContext context: Context,
    ): SavedSearchWordDBHelper {
        return SavedSearchWordDBHelper(context)
    }

    @Provides
    @Singleton
    fun providePlaceRepository(kakaoLocalService: KakaoLocalService): PlaceRepository {
        return PlaceRepositoryImpl(kakaoLocalService)
    }

    @Provides
    @Singleton
    fun provideSavedSearchWordRepository(dbHelper: SavedSearchWordDBHelper): SavedSearchWordRepository {
        return SavedSearchWordRepositoryImpl(dbHelper)
    }

    @Provides
    @Singleton
    fun provideViewModelFactory(
        placeRepository: PlaceRepository,
        savedSearchWordRepository: SavedSearchWordRepository,
    ): ViewModelFactory {
        return ViewModelFactory(placeRepository, savedSearchWordRepository)
    }
}
