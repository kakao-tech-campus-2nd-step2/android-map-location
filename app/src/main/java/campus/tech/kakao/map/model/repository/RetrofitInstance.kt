package campus.tech.kakao.map.model.repository

import android.os.Looper
import campus.tech.kakao.map.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val retrofitClient = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(getOkHttpClient())
        .build()

    private fun getOkHttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder()

        // 가로채서 헤더에 키 추가하기
        val interceptor = object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "KakaoAK " + BuildConfig.KAKAO_REST_API_KEY)
                    .build()
                val response = chain.proceed(request)

                if (response.code != 200) {
                    android.os.Handler(Looper.getMainLooper()).post {
                        // 백그라운드 스레드에서 작업 중 UI 스레드에서 실행하도록 변경
                        // Toast 메세지를 띄우게 하고 싶은데 context 자리에 뭘 넣어야할지 모르겠습니다!!
//                        Toast.makeText(
//                            ,
//                            "Error: ${response.code} - ${response.message}",
//                            Toast.LENGTH_SHORT
//                        ).show()
                    }
                }
                return response
            }
        }
        return client.addInterceptor(interceptor).build()
    }

    fun getInstance(): Retrofit{
        return retrofitClient
    }
}