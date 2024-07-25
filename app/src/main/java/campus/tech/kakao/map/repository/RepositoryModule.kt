package campus.tech.kakao.map.repository

import android.app.Application
import android.content.Context
import campus.tech.kakao.map.base.MyApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }


    @Provides
    @Singleton
    fun providePlaceRepository(context: Context): PlaceRepositoryInterface {
        return PlaceRepository(context.applicationContext as MyApplication)
    }

    @Provides
    @Singleton
    fun provideLogRepository(context: Context): LogRepositoryInterface {
        return LogRepository(context.applicationContext as MyApplication)
    }

    @Provides
    @Singleton
    fun provideMapRepository(context: Context): MapRepositoryInterface {
        return MapRepository(context.applicationContext as MyApplication)
    }
}