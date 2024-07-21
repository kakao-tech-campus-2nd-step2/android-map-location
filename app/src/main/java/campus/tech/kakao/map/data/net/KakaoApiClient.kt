package campus.tech.kakao.map.data.net


import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.domain.model.ResultSearchKeyword
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object KakaoApiClient {
    private const val BASE_URL = "https://dapi.kakao.com/"

    val api: KakaoApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KakaoApi::class.java)
    }
}