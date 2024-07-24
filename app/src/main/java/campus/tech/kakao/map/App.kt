package campus.tech.kakao.map

import android.app.Application
import campus.tech.kakao.map.model.datasource.SharedPreferences
import com.kakao.vectormap.KakaoMapSdk

class App : Application(){
    companion object{
        lateinit var sharedPreferencesManager : SharedPreferences
    }
    override fun onCreate() {
        sharedPreferencesManager = SharedPreferences(applicationContext)
        KakaoMapSdk.init(this, BuildConfig.KAKAO_API_KEY)
        super.onCreate()

    }
}
