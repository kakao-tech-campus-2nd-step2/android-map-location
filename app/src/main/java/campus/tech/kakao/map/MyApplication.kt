package campus.tech.kakao.map

import android.app.Application
import campus.tech.kakao.map.di.AppContainer
import com.kakao.vectormap.KakaoMapSdk

class MyApplication : Application() {
    val appContainer: AppContainer by lazy{
        AppContainer(applicationContext)
    }

    override fun onCreate() {
        KakaoMapSdk.init(this,campus.tech.kakao.map.BuildConfig.KAKAO_API_KEY)
        super.onCreate()
    }
}