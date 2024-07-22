package campus.tech.kakao.map

import android.app.Application
import com.kakao.vectormap.KakaoMapSdk

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoMapSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY +"12")
    }
}