package campus.tech.kakao.map

import android.app.Application
import com.kakao.vectormap.KakaoMapSdk

class MyApplication : Application() {
	override fun onCreate() {
		super.onCreate()
		mapPosition = MapPositionPreferences(this)
		KakaoMapSdk.init(this, BuildConfig.KAKAO_API_KEY)
	}

	companion object{
		lateinit var mapPosition : MapPositionPreferences
	}
}