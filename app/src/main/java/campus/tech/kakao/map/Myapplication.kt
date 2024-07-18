package campus.tech.kakao.map

import android.app.Application
import android.util.Log
import com.kakao.vectormap.KakaoMapSdk

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoMapSdk.init(this, "bfa0e5ab308b39ef7e373921d5a5e697")
        Log.d("KakaoSDK", "Kakao SDK initialized")
    }
}
