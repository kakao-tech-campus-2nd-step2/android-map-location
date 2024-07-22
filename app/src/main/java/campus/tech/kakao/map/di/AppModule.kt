package campus.tech.kakao.map.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import campus.tech.kakao.map.data.SavedSearchWordDBHelper
import campus.tech.kakao.map.data.repository.LocationSerializer
import campus.tech.kakao.map.model.Location
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private val Context.dataStore: DataStore<Location> by dataStore(
        fileName = "location_data.pb",
        serializer = LocationSerializer,
    )

    @Provides
    @Singleton
    fun provideSavedSearchWordDBHelper(
        @ApplicationContext context: Context,
    ): SavedSearchWordDBHelper {
        return SavedSearchWordDBHelper(context)
    }

    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Location> {
        return context.dataStore
    }
}
