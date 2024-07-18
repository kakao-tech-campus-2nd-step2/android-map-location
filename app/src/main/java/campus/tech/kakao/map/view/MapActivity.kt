package campus.tech.kakao.map.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.Model.LocationData
import campus.tech.kakao.map.R
import campus.tech.kakao.map.Adapter.MapViewAdapter
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory

class MapActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var inputText: View
    private lateinit var mapViewAdapter: MapViewAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mapView = findViewById(R.id.map_view)
        inputText = findViewById(R.id.MapinputText)
        recyclerView = findViewById(R.id.recyclerView)


        mapView.start(
            object : MapLifeCycleCallback() {
                override fun onMapDestroy() {}
                override fun onMapError(error: Exception) {
                    Log.e("MapError", "${error.message}", error)
                }
            },
            object : KakaoMapReadyCallback() {
                override fun onMapReady(kakaoMap: KakaoMap) {
                    // 맵 초기화 코드
                    val mapCenter = LatLng.from(37.566, 126.978)  // 서울시청 좌표
                    kakaoMap.moveCamera(CameraUpdateFactory.newCenterPosition(mapCenter))
                    kakaoMap.moveCamera(CameraUpdateFactory.zoomTo(15))
                }
            }
        )

        inputText.setOnClickListener {
            val intent = Intent(this@MapActivity, MainActivity::class.java)
            startActivity(intent)
        }

        val selectedLocation = intent.getStringExtra("selectedLocation")
    }

    override fun onResume() {
        super.onResume()
        mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        mapView.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.finish()
    }

}