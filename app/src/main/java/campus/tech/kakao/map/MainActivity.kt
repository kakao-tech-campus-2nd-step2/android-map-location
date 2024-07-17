package campus.tech.kakao.map

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import campus.tech.kakao.map.R
import campus.tech.kakao.map.SearchActivity
import campus.tech.kakao.map.databinding.ActivityMainBinding
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraUpdateFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("MainActivity", "onCreate called")

        initializeKakaoSdk()
        initializeMapView()
        setSearchBoxClickListener()
    }

    private fun initializeKakaoSdk() {
        val apiKey = getString(R.string.kakao_api_key)
        KakaoMapSdk.init(this, apiKey)
        Log.d("MainActivity", "Kakao API Key: $apiKey")
        Log.d("MainActivity", "KakaoMapSdk initialized")
    }

    private fun initializeMapView() {

        val mapLifeCycleCallback = object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                Log.d("MainActivity", "onMapDestroy called")
            }

            override fun onMapError(error: Exception) {
                Log.e("MainActivity", "onMapError: ${error.message}")
            }
        }

        val kakaoMapReadyCallback = object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                Log.d("MainActivity", "Map is ready")
            }

            override fun getPosition(): LatLng {
                Log.d("MainActivity", "getPosition called")
                return LatLng.from(36.37591485731178, 127.34381616478682)
            }
        }

        binding.mapView.start(mapLifeCycleCallback, kakaoMapReadyCallback)
    }

    private fun setSearchBoxClickListener() {
        binding.searchBar.setOnClickListener {
            Log.d("MainActivity", "Search box clicked")
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("MainActivity", "onResume called")
        binding.mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        Log.d("MainActivity", "onPause called")
        binding.mapView.pause()
    }
}
