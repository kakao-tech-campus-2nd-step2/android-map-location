package campus.tech.kakao.map

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://dapi.kakao.com"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitLocalCategoryService: RetrofitLocalCategoryService = retrofit.create(RetrofitLocalCategoryService::class.java)
    val retrofitLocalKeywordService: RetrofitLocalKeywordService = retrofit.create(RetrofitLocalKeywordService::class.java)
}