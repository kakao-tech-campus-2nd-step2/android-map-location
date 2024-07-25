package campus.tech.kakao.map.Activity

import android.app.Application
import campus.tech.kakao.map.R
import com.kakao.vectormap.KakaoMapSdk

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoMapSdk.init(this, getString(R.string.kakao_api_key))
        //KakaoMapSdk.init(this, "123") // 에러용 코드
    }
}