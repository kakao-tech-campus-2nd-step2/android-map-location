package campus.tech.kakao.map

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val retrofitService = Retrofit.Builder()
        .baseUrl("https://dapi.kakao.com/v2/local/search/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RetrofitService::class.java)
}