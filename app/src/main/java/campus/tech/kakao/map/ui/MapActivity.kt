package campus.tech.kakao.map.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import campus.tech.kakao.map.R
import com.google.android.material.bottomsheet.BottomSheetDialog
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
    private var kakaoMap: KakaoMap? = null
    var savedLatitude: Double = 37.3957122          // MapActivity Unit 테스트를 위해 public으로 변경
    var savedLongitude: Double = 127.1105181        // // MapActivity Unit 테스트를 위해 public으로 변경
    private lateinit var errorLayout: View
    private lateinit var searchLayout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mapView = findViewById(R.id.mapView)
        errorLayout = findViewById(R.id.errorLayout)
        searchLayout = findViewById(R.id.searchLayout)
        loadSavedLocation()

        mapView.start(mapLifeCycleCallback, kakaoMapReadyCallback)

        val etMapSearch = findViewById<EditText>(R.id.etMapSearch)
        etMapSearch.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
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
        saveCurrentLocation()
    }

    private val mapLifeCycleCallback = object : MapLifeCycleCallback() {
        override fun onMapDestroy() {
            Log.d("MapActivity", getString(R.string.onMapDestroyLog))
        }

        override fun onMapError(error: Exception) {
            Log.e("MapActivity", getString(R.string.onMapErrorLog))
            showErrorLayout(error.message)
        }
    }

    private val kakaoMapReadyCallback = object : KakaoMapReadyCallback() {
        override fun onMapReady(map: KakaoMap) {
            Log.d("MapActivity", getString(R.string.onMapReadyLog))
            kakaoMap = map
            updateCameraPosition(savedLatitude, savedLongitude)

            val name = intent.getStringExtra("name")
            val address = intent.getStringExtra("address")
            val longitude = intent.getDoubleExtra("longitude", 0.0)
            val latitude = intent.getDoubleExtra("latitude", 0.0)

            if (latitude != 0.0 && longitude != 0.0) {
                addMarker(latitude, longitude, name ?: "")
                saveLocation(latitude, longitude)
                showBottomSheet(name ?: "", address ?: "")
            }
        }
    }

    private fun saveLocation(latitude: Double, longitude: Double) {
        savedLatitude = latitude
        savedLongitude = longitude
        saveDataToPreferences(latitude.toString(), longitude.toString())
    }

    // MapActivity Unit 테스트를 위해 public으로 변경
    fun saveCurrentLocation() {
        saveDataToPreferences(savedLatitude.toString(), savedLongitude.toString())
    }

    private fun saveDataToPreferences(latitude: String, longitude: String) {
        val preferences = getSharedPreferences("location_prefs", MODE_PRIVATE)
        preferences.edit().apply {
            putString("latitude", latitude)
            putString("longitude", longitude)
            apply()
        }
    }

    // MapActivity Unit 테스트를 위해 public으로 변경
    fun loadSavedLocation() {
        val preferences = getSharedPreferences("location_prefs", MODE_PRIVATE)
        savedLatitude = preferences.getString("latitude", "37.3957122")?.toDouble() ?: 37.3957122
        savedLongitude = preferences.getString("longitude", "127.1105181")?.toDouble() ?: 127.1105181
        //Log.d("MapActivity", "Loaded Latitude: $savedLatitude, Longitude: $savedLongitude")
    }

    private fun updateCameraPosition(latitude: Double, longitude: Double) {
        val cameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(latitude, longitude))
        kakaoMap?.moveCamera(cameraUpdate)
    }

    private fun addMarker(latitude: Double, longitude: Double, name: String) {
        val labelManager = kakaoMap?.labelManager
        val iconAndTextStyle = LabelStyles.from(
            LabelStyle.from(R.drawable.gps).setTextStyles(30, Color.BLACK)
        )
        val options = LabelOptions.from(LatLng.from(latitude, longitude))
            .setStyles(iconAndTextStyle)
        val layer = labelManager?.layer
        val label = layer?.addLabel(options)
        label?.changeText(name)

        updateCameraPosition(latitude, longitude)
    }

    private fun showBottomSheet(name: String, address: String) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet, null)
        bottomSheetDialog.setContentView(bottomSheetView)

        val tvPlaceName = bottomSheetView.findViewById<TextView>(R.id.tvPlaceName)
        val tvPlaceAddress = bottomSheetView.findViewById<TextView>(R.id.tvPlaceAddress)

        tvPlaceName.text = name
        tvPlaceAddress.text = address

        bottomSheetDialog.show()
    }

    private fun showErrorLayout(message: String?) {
        val errorMessage = errorLayout.findViewById<TextView>(R.id.tvErrorMessage)
        errorMessage.text="$message"
        errorLayout.visibility = View.VISIBLE
        searchLayout.visibility = View.GONE
        mapView.visibility = View.GONE
    }
}