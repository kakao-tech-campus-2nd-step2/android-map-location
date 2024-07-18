package campus.tech.kakao.Model

import okhttp3.HttpUrl
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClientTest {

    @Test
    fun testRetrofitInstanceCreation() {
        val apiService: KakaoApiService = RetrofitClient.instance

        assertNotNull("KakaoApiService instance not gonna be null", apiService)
        val retrofit = Retrofit.Builder()
            .baseUrl(RetrofitClient.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val baseUrl: HttpUrl = retrofit.baseUrl()
        assertEquals("baseUrls gonna be same with expected url", RetrofitClient.BASE_URL, baseUrl.toString())
    }
}