package campus.tech.kakao.map

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.kakao.vectormap.KakaoMapSdk

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "last_location_datastore")

class KakaoMapApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoMapSdk.init(this, getString(R.string.kakao_api_key))
    }
}