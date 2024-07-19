package campus.tech.kakao.map

import campus.tech.kakao.map.model.RetrofitInstance
import okhttp3.HttpUrl
import org.junit.Assert
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitTest {

    @Test
    fun retrofitTest() {
        // Retrofit 객체를 새로 생성하여 base URL을 설정합니다.
        val retrofit = Retrofit.Builder()
            .baseUrl(RetrofitInstance.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // 새로 생성한 Retrofit 객체의 base URL을 가져옵니다.
        val baseUrl: HttpUrl = retrofit.baseUrl()

        // RetrofitClient의 BASE_URL과 새로 생성한 Retrofit 객체의 base URL이 동일함을 검증합니다.
        Assert.assertEquals("baseUrls gonna be same with expected url", RetrofitInstance.BASE_URL, baseUrl.toString())
    }
}