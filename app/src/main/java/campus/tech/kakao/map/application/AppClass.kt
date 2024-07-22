package campus.tech.kakao.map.application

import android.app.Application
import campus.tech.kakao.map.R
import com.kakao.vectormap.KakaoMapSdk

class AppClass : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoMapSdk.init(this, getString(R.string.kakao_api_key))
    }
}