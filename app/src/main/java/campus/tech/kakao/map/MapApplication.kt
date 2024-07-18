package campus.tech.kakao.map

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import com.kakao.vectormap.KakaoMapSdk

class MapApplication: Application() {
    companion object {
        lateinit var prefs: PreferenceManager
    }

    override fun onCreate() {
        val apiKey = getString(R.string.kakao_api_key)
        prefs = PreferenceManager(applicationContext)
        super.onCreate()
        KakaoMapSdk.init(this, apiKey)
    }
}