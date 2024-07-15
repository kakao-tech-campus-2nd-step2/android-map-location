package campus.tech.kakao.map

import kotlinx.coroutines.coroutineScope
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchRepository {
    companion object {
        const val BASE_URL = "https://dapi.kakao.com"
        const val API_KEY = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}"
    }

    private val retrofitKakaoSearchKeyword = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(KakaoSearchKeywordAPI::class.java)

    suspend fun Search(searchKeyword: SearchKeyword): List<Place> {
        if (searchKeyword.searchKeyword == "")
            return emptyList()

        return coroutineScope {
            retrofitKakaoSearchKeyword
                .getSearchKeyWord(API_KEY, searchKeyword.searchKeyword)
                .places
        }
    }
}