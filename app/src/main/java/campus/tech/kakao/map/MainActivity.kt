package campus.tech.kakao.map

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import campus.tech.kakao.map.databinding.ActivityMainBinding
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.label.Label
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import okhttp3.Address

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewBinding: ActivityMainBinding
    private lateinit var mapView: MapView
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory(applicationContext, MapApplication.prefs)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainViewBinding.root)

        val latitude = intent?.getStringExtra("latitude")?.toDoubleOrNull()
        val longitude = intent?.getStringExtra("longitude")?.toDoubleOrNull()
        val name = intent?.getStringExtra("name")
        val address = intent?.getStringExtra("address")

        mapView = mainViewBinding.mapView

        setUpMapView(mapView, longitude, latitude)
        setBottomSheet(name, address)

        mainViewBinding.search.setOnClickListener {
            val intent = Intent(this@MainActivity, SearchActivity::class.java)
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

    private fun setUpMapView(mapView: MapView, longitude: Double?, latitude: Double?) {

        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                // 지도 파괴 시 처리
            }

            override fun onMapError(error: Exception) {
                val intent = Intent(this@MainActivity, ErrorActivity::class.java)
                intent.putExtra("Error", error.localizedMessage)
                startActivity(intent)
            }

        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                val labelManager = kakaoMap.labelManager
                val styles: LabelStyles? = labelManager?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.map_pin)))
                val options = LabelOptions.from(position).setStyles(styles)
                val layer = kakaoMap.labelManager?.layer
                val label = layer?.addLabel(options)
            }

            override fun isVisible(): Boolean {
                return true
            }

            override fun getZoomLevel(): Int {
                return 18
            }

            override fun getPosition(): LatLng {
                return mainViewModel.setLocation(latitude, longitude) ?: super.getPosition()
            }
        })
    }

    private fun setBottomSheet(name: String?, address: String?) {
        if (!name.isNullOrEmpty() && !address.isNullOrEmpty()) {
            mainViewBinding.place.text = name
            mainViewBinding.address.text = address
            mainViewBinding.mapBottomSheet.visibility = View.VISIBLE
        } else {
            mainViewBinding.mapBottomSheet.visibility = View.GONE
        }
    }

}
