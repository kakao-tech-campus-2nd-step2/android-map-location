package campus.tech.kakao.map.data.source

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitServiceClient {
    val retrofitService: RetrofitService =
        Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitService::class.java)

//    fun getRetrofit(baseUrl: String): RetrofitService {
//        retrofitService = Retrofit.Builder()
//            .baseUrl(baseUrl)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(RetrofitService::class.java)
//        return retrofitService
//    }
}