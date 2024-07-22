package campus.tech.kakao.map

import android.app.Application
import com.kakao.vectormap.KakaoMapSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeKakaoMapSdk()
    }

    /**
     * Kakao Map SDK를 초기화하는 함수.
     */
    private fun initializeKakaoMapSdk() {
        KakaoMapSdk.init(this, BuildConfig.KAKAO_API_KEY)
    }
}
