package campus.tech.kakao.map

import android.app.Application
import com.kakao.vectormap.KakaoMapSdk

class Application : Application() {
    companion object{
        lateinit var prefs: PreferenceUtil
    }
    override fun onCreate(){
        prefs = PreferenceUtil(applicationContext)
        super.onCreate()

        //카카오맵 SDK 초기화
        val key = getString(R.string.kakao_api_key)
        KakaoMapSdk.init(this, key)
    }
}