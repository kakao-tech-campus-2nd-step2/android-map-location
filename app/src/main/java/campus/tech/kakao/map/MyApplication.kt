package campus.tech.kakao.map

import android.app.Application
import com.kakao.vectormap.KakaoMapSdk

class MyApplication : Application() {
	override fun onCreate() {
		super.onCreate()
		KakaoMapSdk.init(this, BuildConfig.KAKAO_API_KEY)
	}

	companion object {
		val mapPosition: MapPositionPreferences by lazy {
			MapPositionPreferences(getInstance())
		}

		private fun getInstance(): MyApplication {
			return this as MyApplication
		}
	}
}