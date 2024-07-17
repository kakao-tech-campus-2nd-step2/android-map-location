package campus.tech.kakao.map

import android.app.Application
import com.kakao.vectormap.KakaoMapSdk

class MapApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val key = getString(R.string.kakao_api_key)
        KakaoMapSdk.init(this, key)
    }
}
