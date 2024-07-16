package campus.tech.kakao.map.data.net


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitApiClient {
    private const val BASE_URL = "https://dapi.kakao.com/"

    val api: KakaoAPI by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KakaoAPI::class.java)
    }
}