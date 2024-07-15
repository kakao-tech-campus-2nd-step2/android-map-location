package campus.tech.kakao.map.di

import campus.tech.kakao.map.data.network.KaKaoLocalApiClient
import campus.tech.kakao.map.data.network.service.KakaoLocalService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideKakaoLocalService(): KakaoLocalService {
        return KaKaoLocalApiClient.retrofit.create(KakaoLocalService::class.java)
    }
}
