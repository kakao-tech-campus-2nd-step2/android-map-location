package campus.tech.kakao.map.Model

import android.app.Application
import android.util.Log
import campus.tech.kakao.map.BuildConfig
import com.kakao.vectormap.KakaoMapSdk

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        try {
            KakaoMapSdk.init(this, BuildConfig.MAP_API_KEY)
            Log.d("KakaoMapSDK", "SDK initialized successfully")
        } catch (e: Exception) {
            Log.e("KakaoMapSDK", "error", e)
        }

    }
}