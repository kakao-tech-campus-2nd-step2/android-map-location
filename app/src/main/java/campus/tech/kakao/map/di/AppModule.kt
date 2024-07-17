package campus.tech.kakao.map.di

import android.content.Context
import campus.tech.kakao.map.data.SavedSearchWordDBHelper
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
}
