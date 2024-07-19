package campus.tech.kakao.map

import android.app.Application
import com.kakao.vectormap.KakaoMapSdk

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoMapSdk.init(this, getString(R.string.kakao_api_key))
        //KakaoMapSdk.init(this, "123") // 에러용 코드
    }
}