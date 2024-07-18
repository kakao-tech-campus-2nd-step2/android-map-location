package campus.tech.kakao.map

import android.app.Application
import com.kakao.vectormap.KakaoMapSdk

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val appKey = BuildConfig.KAKAO_API_KEY
        KakaoMapSdk.init(this, appKey)
    }
}