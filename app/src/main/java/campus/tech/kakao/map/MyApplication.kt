package campus.tech.kakao.map

import android.app.Application
import campus.tech.kakao.map.dto.MapPosition
import com.kakao.vectormap.KakaoMapSdk

class MyApplication : Application() {
	override fun onCreate() {
		super.onCreate()
		MapPosition.getMapPosition(this)
		KakaoMapSdk.init(this, BuildConfig.KAKAO_API_KEY)
	}

}