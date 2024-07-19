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
import campus.tech.kakao.map.Adapter.MapViewAdapter
import campus.tech.kakao.map.Model.LocationData
import campus.tech.kakao.map.R
import campus.tech.kakao.map.viewmodel.MapViewModel
import com.google.gson.Gson
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles

class MapActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var inputText: View
    private lateinit var recyclerView: RecyclerView

    private var selectedLocation: LocationData? = null

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
            selectedLocation = Gson().fromJson(selectedLocationJson, LocationData::class.java)
            mapViewModel.setMapViewAdapter(listOf(selectedLocation!!))
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
                    selectedLocation?.let { location ->
                        val selectedLatLng = location.getLatLng()

                        Log.d("MapActivity", "Selected Location: $location")
                        Log.d("MapActivity", "Selected LatLng: $selectedLatLng")

                        kakaoMap.moveCamera(CameraUpdateFactory.newCenterPosition(selectedLatLng))
                        kakaoMap.moveCamera(CameraUpdateFactory.zoomTo(15))

                        val styles = kakaoMap.labelManager?.addLabelStyles(
                            LabelStyles.from(LabelStyle.from(R.drawable.location_icon))
                        )

                        val options = LabelOptions.from(selectedLatLng).setStyles(styles)

                        val labelManager = kakaoMap.labelManager
//                        val label = labelManager?.addLabel(options)

                    } ?: run {
                        val defaultLatLng = LatLng.from(37.566, 126.978)
                        kakaoMap.moveCamera(CameraUpdateFactory.newCenterPosition(defaultLatLng))
                        kakaoMap.moveCamera(CameraUpdateFactory.zoomTo(15))
                    }
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
