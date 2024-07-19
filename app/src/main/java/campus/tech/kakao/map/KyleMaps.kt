package campus.tech.kakao.map

import android.app.Application
import campus.tech.kakao.map.repository.KakaoRepository
import com.kakao.vectormap.KakaoMapSdk
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class KyleMaps : Application() {

    lateinit var kakaoRepository: KakaoRepository
    var isTestMode: Boolean = false

    override fun onCreate() {
        super.onCreate()
        KakaoMapSdk.init(this, getString(R.string.kakao_api_key))
        val retrofit = Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        kakaoRepository = KakaoRepository(retrofit)
    }
}


