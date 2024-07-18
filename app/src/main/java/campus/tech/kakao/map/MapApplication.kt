package campus.tech.kakao.map

import android.app.Application
import campus.tech.kakao.map.data.repository.HistoryRepositoryImpl
import campus.tech.kakao.map.data.repository.LastLocationRepositoryImpl
import campus.tech.kakao.map.data.repository.ResultRepositoryImpl
import campus.tech.kakao.map.data.source.MapDbHelper
import campus.tech.kakao.map.data.source.RetrofitServiceClient
import com.kakao.vectormap.KakaoMapSdk

class MapApplication : Application() {
    lateinit var dbHelper: MapDbHelper
    lateinit var resultRepositoryImpl: ResultRepositoryImpl
    lateinit var historyRepositoryImpl: HistoryRepositoryImpl
    lateinit var lastLocationRepositoryImpl: LastLocationRepositoryImpl

    override fun onCreate() {
        super.onCreate()
        KakaoMapSdk.init(this, getString(R.string.kakao_api_key))
        dbHelper = MapDbHelper(this)
        resultRepositoryImpl = ResultRepositoryImpl(RetrofitServiceClient.retrofitService)
        historyRepositoryImpl = HistoryRepositoryImpl(dbHelper)
        lastLocationRepositoryImpl = LastLocationRepositoryImpl(dbHelper)
    }
}