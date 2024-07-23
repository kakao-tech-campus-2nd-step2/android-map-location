package campus.tech.kakao.map

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.google.android.gms.maps.CameraUpdateFactory
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import java.lang.Exception

class Map_Activity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var searchView: SearchView
    private val LOCATION_PERMISSION_REQUEST_CODE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mapView = findViewById(R.id.map_view)
        searchView = findViewById(R.id.search_text)

        mapView = MapView(this)
        mapView.start(
            object : MapLifeCycleCallback() {
                override fun onMapDestroy() {
                    Log.d("map", "Map Destroy")
                }

                override fun onMapError(error: Exception) {
                    Log.d("map", "지도가 정상적으로 호출되지 않음")
                }
            },
            object : KakaoMapReadyCallback() {
                override fun onMapReady(kakaoMap: KakaoMap) {
                    val mapCenter = com.google.android.gms.maps.model.LatLng(
                        37.5665,
                        126.9780
                    ) // 예시: 서울의 위도, 경도
                }
            }
        )

        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val intent = Intent(this, Search_Activity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        mapView.pause()
    }
}


