package campus.tech.kakao.map

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import campus.tech.kakao.map.databinding.ActivityMainBinding
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView


class MainActivity : AppCompatActivity() {
    private lateinit var mainViewBinding: ActivityMainBinding
    private lateinit var mapView: MapView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainViewBinding.root)

        mapView = mainViewBinding.mapView

        setUpMapView(mapView)

        mainViewBinding.search.setOnClickListener {
            val intent = Intent(this@MainActivity,SearchActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onPause() {
        super.onPause()
        mapView.pause()
    }

    override fun onResume() {
        super.onResume()
        mapView.resume()
    }

    private fun setUpMapView(mapView: MapView) {
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
            }

            override fun onMapError(error: Exception) {

            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {

            }

            override fun isVisible(): Boolean {
                return true
            }
        })
    }
}
