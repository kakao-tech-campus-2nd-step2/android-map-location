package campus.tech.kakao.map.data.network

import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.data.network.interceptor.AuthInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

object KaKaoLocalApiClient {
    @OptIn(ExperimentalSerializationApi::class)
    val retrofit: Retrofit by lazy {
        val contentType = "application/json".toMediaType()
        val json = Json { ignoreUnknownKeys = true }

        val okHttpClient =
            OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(BuildConfig.KAKAO_REST_API_KEY))
                .build()

        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }
}
