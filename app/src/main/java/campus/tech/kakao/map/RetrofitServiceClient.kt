package campus.tech.kakao.map

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitServiceClient {
    private lateinit var retrofitService: RetrofitService

    fun getRetrofit(baseUrl: String): RetrofitService {
        retrofitService = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitService::class.java)
        return retrofitService
    }
}