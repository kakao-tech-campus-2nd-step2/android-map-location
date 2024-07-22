package campus.tech.kakao.map.ui

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import campus.tech.kakao.map.R
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelManager
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles

class MapActivity : AppCompatActivity() {

    lateinit var mapView: MapView
    lateinit var etSearch: EditText
    private lateinit var labelManager: LabelManager
    private val startZoomLevel = 15
    var latitude: String? = "35.234"
    var longitude: String? = "129.0807"
    private val startPosition = LatLng.from(latitude!!.toDouble(), longitude!!.toDouble())

    private val readyCallback: KakaoMapReadyCallback = object : KakaoMapReadyCallback() {
        override fun onMapReady(kakaoMap: KakaoMap) {
            labelManager = kakaoMap.labelManager!!

            val (preferencesLatitude, preferencesLongitude) = getLocationByPreference()
            val (intentLatitude, intentLongitude) = getLocationByIntent()
            latitude = intentLatitude ?: preferencesLatitude
            longitude = intentLongitude ?: preferencesLongitude

            displayMapLocation(kakaoMap)

            if (detectNotInitialScreen(intentLatitude, intentLongitude)) {
                displayMarker()
                displayBottomSheet()
            }

            saveLocation()
        }

        override fun getPosition(): LatLng {
            return startPosition
        }

        override fun getZoomLevel(): Int {
            return startZoomLevel
        }

    }

    private fun detectNotInitialScreen(intentLatitude: String?, intentLongitude: String?) =
        intentLatitude != null && intentLongitude != null

    private fun displayMapLocation(kakaoMap: KakaoMap) {
        val cameraUpdate = CameraUpdateFactory.newCenterPosition(
            LatLng.from(
                longitude!!.toDouble(),
                latitude!!.toDouble()
            )
        )
        kakaoMap.moveCamera(cameraUpdate, CameraAnimation.from(500, true, true))
    }

    private fun getLocationByIntent(): Pair<String?, String?> {
        val intentLatitude = intent.getStringExtra("latitude")
        val intentLongitude = intent.getStringExtra("longitude")
        return Pair(intentLatitude, intentLongitude)
    }

    private fun getLocationByPreference(): Pair<String?, String?> {
        val preferences: SharedPreferences = getSharedPreferences("locationInfo", MODE_PRIVATE)
        val preferencesLatitude = preferences.getString("latitude", null)
        val preferencesLongitude = preferences.getString("longitude", null)
        return Pair(preferencesLatitude, preferencesLongitude)
    }

    fun saveLocation() {
        val preferences: SharedPreferences = getSharedPreferences("locationInfo", MODE_PRIVATE)
        val editor: Editor = preferences.edit()
        editor.putString("latitude", latitude)
        editor.putString("longitude", longitude)
        editor.apply()
    }

    fun displayBottomSheet() {
        val name = intent.getStringExtra("name").toString()
        val address = intent.getStringExtra("address").toString()
        val dataBundle = Bundle().apply {
            putString("name", name)
            putString("address", address)
        }
        val modal = ModalBottomSheet()
        modal.arguments = dataBundle
        modal.show(supportFragmentManager, "modalBottomSheet")
    }

    fun displayMarker() {
        val pos = LatLng.from(longitude!!.toDouble(), latitude!!.toDouble())
        val yellowMarker = labelManager.addLabelStyles(
            LabelStyles.from("yellowMarker", LabelStyle.from(R.drawable.yellow_marker))
        )
        labelManager.layer!!.addLabel(
            LabelOptions.from("label", pos)
                .setStyles(yellowMarker)
        )
    }

    private val lifeCycleCallback: MapLifeCycleCallback = object : MapLifeCycleCallback() {

        override fun onMapDestroy() {
            Toast.makeText(
                applicationContext, "onMapDestroy",
                Toast.LENGTH_SHORT
            ).show()
        }

        override fun onMapError(error: Exception) {
            setContentView(R.layout.error_layout)
            val tvError = findViewById<TextView>(R.id.tvError)
            val btnRefresh = findViewById<ImageButton>(R.id.btnRefresh)
            tvError.text = "지도 인증을 실패했습니다.\n 다시 시도해주세요.\n ${error.message}"
            btnRefresh.setOnClickListener {
                initializeMap()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_layout)
        mapView = findViewById(R.id.mapView)
        etSearch = findViewById(R.id.etSearch)
        mapView.start(lifeCycleCallback, readyCallback)
        etSearch.setOnClickListener {
            val searchIntent = Intent(this, PlaceActivity::class.java)
            startActivity(searchIntent)
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

    private fun initializeMap() {
        mapView.start(lifeCycleCallback, readyCallback)
    }
}