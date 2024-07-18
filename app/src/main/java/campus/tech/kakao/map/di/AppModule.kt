package campus.tech.kakao.map.di

import android.content.Context
import android.content.SharedPreferences
import campus.tech.kakao.map.data.SavedSearchWordDBHelper
import campus.tech.kakao.map.data.repository.LocationRepository
import campus.tech.kakao.map.data.repository.LocationRepositoryImpl
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
    fun provideSharedPreferences(
        @ApplicationContext context: Context,
    ): SharedPreferences {
        return context.getSharedPreferences(LocationRepositoryImpl.PREF_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideLocationRepository(sharedPreferences: SharedPreferences): LocationRepository {
        return LocationRepositoryImpl(sharedPreferences)
    }
}
