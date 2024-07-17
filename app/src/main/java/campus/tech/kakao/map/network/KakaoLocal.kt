package campus.tech.kakao.map.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object KakaoLocal {
    private const val BASE_URL = "https://dapi.kakao.com"

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
