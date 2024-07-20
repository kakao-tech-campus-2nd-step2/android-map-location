package campus.tech.kakao.map.base

import android.app.Application
import campus.tech.kakao.map.BuildConfig
import com.kakao.vectormap.KakaoMapSdk

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val appKey = BuildConfig.KAKAO_API_KEY
        KakaoMapSdk.init(this, appKey)
    }
}