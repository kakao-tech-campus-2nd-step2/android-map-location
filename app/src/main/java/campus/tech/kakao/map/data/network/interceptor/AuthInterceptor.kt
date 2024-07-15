package campus.tech.kakao.map.data.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val requestBuilder =
            original.newBuilder()
                .header("Authorization", "KakaoAK $apiKey")
                .method(original.method, original.body)
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
