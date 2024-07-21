package campus.tech.kakao.Model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
private const val BASE_URL = "https://dapi.kakao.com/"

        val instance: KakaoApiService by lazy {
        val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

        retrofit.create(KakaoApiService::class.java)
        }
        }