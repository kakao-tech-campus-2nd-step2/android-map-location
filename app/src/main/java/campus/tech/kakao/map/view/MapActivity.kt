package campus.tech.kakao.map.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.Model.LocationData
import campus.tech.kakao.map.R
import campus.tech.kakao.map.Adapter.MapViewAdapter
import campus.tech.kakao.map.viewmodel.MapViewModel
import com.google.gson.Gson
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory

class MapActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var inputText: View
    private lateinit var recyclerView: RecyclerView

    private val mapViewModel: MapViewModel by viewModels()
    private lateinit var mapViewAdapter: MapViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mapView = findViewById(R.id.map_view)
        inputText = findViewById(R.id.MapinputText)
        recyclerView = findViewById(R.id.recyclerView)

        val selectedLocationJson = intent.getStringExtra("selectedLocation")
        if (selectedLocationJson != null) {
            val selectedLocation = Gson().fromJson(selectedLocationJson, LocationData::class.java)
            mapViewModel.setMapViewAdapter(listOf(selectedLocation))
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        mapViewAdapter = MapViewAdapter(emptyList())
        recyclerView.adapter = mapViewAdapter

        mapViewModel.locationData.observe(this, Observer { locations ->
            mapViewAdapter.updateData(locations)
        })

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