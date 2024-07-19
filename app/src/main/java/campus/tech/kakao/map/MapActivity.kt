package campus.tech.kakao.map

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles

class MapActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var searchButton: LinearLayout
    private lateinit var bottomSheet: BottomSheetDialog

    private var name: String? = null
    private var address: String? = null
    private var kakaoMap: KakaoMap? = null
    private var latitude: String? = null
    private var longitude: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("here", "I'm in MapActivity")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mapView = findViewById(R.id.map_view)
        searchButton = findViewById(R.id.search_button)
        val bottomSheetLayout = layoutInflater.inflate(R.layout.layout_map_bottom_sheet_dialog, null)
        bottomSheet = BottomSheetDialog(this)

        val defLat = "35.231627"
        val defLng = "129.084020"
        val lastLatLng = getSharedPreferences("lastLatLng", MODE_PRIVATE)
        latitude = lastLatLng.getString("lastLat", defLat) // 기본 위치: 부산대.
        longitude = lastLatLng.getString("lastLng", defLng) // 기본 위치: 부산대.
        Log.d("sharedPreference", "saveLatLng밖에꺼: $latitude")
        Log.d("sharedPreference", "saveLatLng밖에꺼: $longitude")

        // Intent에서 값을 가져옴
        intent?.let {
            latitude = it.getStringExtra("mapY") ?: latitude
            longitude = it.getStringExtra("mapX") ?: longitude
            name = it.getStringExtra("name")
            address = it.getStringExtra("address")
        }

        Log.d("latlngcheck1", "Intent에서 가져온 값: $latitude, $longitude, $name")

        mapView.start(
            object : MapLifeCycleCallback() {
                override fun onMapDestroy() {
                }

                override fun onMapError(error: Exception) {
                    Log.e("MapActivity", "Map error: ${error.message}")
                    val errorIntent = Intent(this@MapActivity, AuthenticationErrorActivity::class.java)
                    startActivity(errorIntent)
                    finish()
                }

            },
            object : KakaoMapReadyCallback() {
                override fun onMapReady(kakaoMap: KakaoMap) {
                    Log.d("latlngcheck2", "onMapReady에서: $latitude, $longitude")
                    this@MapActivity.kakaoMap = kakaoMap
                    name?.let {
                        addMarker(kakaoMap, latitude ?: defLat, longitude ?: defLng, it)
                        saveLatLng(latitude ?: defLat, longitude ?: defLng) // 마커 찍은 위치를 sharedPreference에 저장
                        val tvBottomSheetPlaceName = bottomSheetLayout.findViewById<TextView>(R.id.bottomSheetPlaceName)
                        val tvBottomSheetPlaceAddress = bottomSheetLayout.findViewById<TextView>(R.id.bottomsheetPlaceAddress)
                        tvBottomSheetPlaceName.text = name
                        tvBottomSheetPlaceAddress.text = address
                        bottomSheet.setContentView(bottomSheetLayout)
                        bottomSheet.show()
                    }
                }

                override fun getPosition(): LatLng {
                    Log.d("latlngcheck3", "getPosition에서: $latitude, $longitude")
                    val lat = (latitude ?: defLat).toDouble()
                    val lng = (longitude ?: defLng).toDouble()
                    return LatLng.from(lat, lng)
                }
            }
        )

        searchButton.setOnClickListener {
            val mapToSearchIntent = Intent(this@MapActivity, SearchActivity::class.java)
            startActivity(mapToSearchIntent)
            finish()
        }
    }

    private fun addMarker(kakaoMap: KakaoMap, latitude: String, longitude: String, name: String) {
        Log.d("addMarker", "addMarker: $latitude, $longitude, $name")
        val styles = kakaoMap.labelManager?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.location_marker).setTextStyles(25, Color.BLACK)))
        val options = LabelOptions.from(LatLng.from(latitude.toDouble(), longitude.toDouble())).setStyles(styles)
        val layer = kakaoMap.labelManager?.layer
        val label = layer?.addLabel(options)
        label?.changeText(name)
    }

    fun saveLatLng(latitude: String, longitude: String) {
        val sharedPreference = getSharedPreferences("lastLatLng", MODE_PRIVATE)
        sharedPreference.edit().apply {
            putString("lastLat", latitude)
            putString("lastLng", longitude)
            apply()
        }
        Log.d("sharedPreference", "saveLatLng: ${sharedPreference.getString("lastLat", "default_lat")}")
        Log.d("sharedPreference", "saveLatLng: ${sharedPreference.getString("lastLng", "default_lng")}")
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
