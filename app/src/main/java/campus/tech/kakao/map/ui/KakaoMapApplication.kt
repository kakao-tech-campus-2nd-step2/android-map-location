package campus.tech.kakao.map.ui

import android.app.Application
import campus.tech.kakao.map.BuildConfig
import com.kakao.vectormap.KakaoMapSdk

class KakaoMapApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoMapSdk.init(this, BuildConfig.KAKAO_API_KEY)
    }
}