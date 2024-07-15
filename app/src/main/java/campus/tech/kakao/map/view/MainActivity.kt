package campus.tech.kakao.map.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityMainBinding
import com.kakao.sdk.common.util.Utility
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initKakaoMap()

        binding.searchEditText.isFocusable = false
        binding.searchEditText.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("testt", "onResume")
        mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        Log.d("testt", "onPause")
        mapView.pause()
    }

    private fun initKakaoMap() {
        val keyHash = Utility.getKeyHash(this)
        Log.d("testt", keyHash)
        val key = getString(R.string.kakao_api_key)
        KakaoMapSdk.init(this, key)

        mapView = binding.mapView
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                Log.d("testt", "onMapDestory")
            }

            override fun onMapError(error: Exception) {
                Log.d("testt", "onMapError")
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                Log.d("testt", "onMapReady")
            }
        })
    }
}
