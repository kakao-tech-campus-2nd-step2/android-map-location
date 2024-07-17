package campus.tech.kakao.map.repository

import android.util.Log
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.model.Place
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class KakaoApiDataSource {
    object KakaoRetrofitInstance {

        val kakaoLocalApi : KakaoLocalApi = getApiClient().create()

        private fun getApiClient(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BuildConfig.KAKAO_BASE_URL)
                .client(provideOkHttpClient(AppInterceptor()))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        private fun provideOkHttpClient(interceptor: AppInterceptor): OkHttpClient
                = OkHttpClient.Builder().run {
            addInterceptor(interceptor)
            build()
        }

        class AppInterceptor : Interceptor {
            override fun intercept(chain: Interceptor.Chain) : okhttp3.Response = with(chain) {
                val newRequest = request().newBuilder()
                    .addHeader("Authorization", BuildConfig.KAKAO_LOCAL_API_KEY)
                    .build()
                proceed(newRequest)
            }
        }
    }

    suspend fun getPlaceData(text: String) : List<Place> {
        val emptyList = listOf<Place>()
        val kakaoApi = KakaoRetrofitInstance.kakaoLocalApi
        return try{
            val placeList = kakaoApi.getPlaceData(text)
            Log.d("coroutineTest", "return")
            placeList.documents ?: emptyList
        } catch (e : Exception){
            Log.d("coroutineTest", e.toString())
            emptyList
        }
    }
}