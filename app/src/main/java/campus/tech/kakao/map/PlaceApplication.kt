package campus.tech.kakao.map

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import campus.tech.kakao.map.data.PlaceRepositoryImpl
import campus.tech.kakao.map.domain.repository.PlaceRepository
import com.kakao.vectormap.KakaoMapSdk

class PlaceApplication: Application() {

    val placeRepository: PlaceRepository by lazy { PlaceRepositoryImpl.getInstance(this)}

    override fun onCreate() {
        super.onCreate()
        appInstance = this

        val key = getString(R.string.kakao_api_key)
        KakaoMapSdk.init(this, key)
    }
    companion object {
        @Volatile
        private lateinit var appInstance: PlaceApplication
        fun isNetworkActive(): Boolean {
            val connectivityManager: ConnectivityManager =
                appInstance.getSystemService(ConnectivityManager::class.java)
            val network: Network = connectivityManager.activeNetwork ?: return false
            val actNetwork: NetworkCapabilities =
                connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                else -> false
            }
        }
    }
}
