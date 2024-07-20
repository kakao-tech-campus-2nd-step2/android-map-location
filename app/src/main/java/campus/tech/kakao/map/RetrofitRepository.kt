package campus.tech.kakao.map

import android.util.Log
import campus.tech.kakao.map.RetrofitInstance.retrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class RetrofitRepository {
    open suspend fun getPlace(query: String): List<Document> {
        if (query.isEmpty()) {
            return emptyList()
        }

        return try {
            val response: Response<Location> = retrofitService.getPlaces(
                "KakaoAK " + BuildConfig.KAKAO_REST_API_KEY,
                query
            )
            if (response.isSuccessful) {
                response.body()?.documents ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}