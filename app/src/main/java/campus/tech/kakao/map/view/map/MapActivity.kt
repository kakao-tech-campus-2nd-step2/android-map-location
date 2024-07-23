package campus.tech.kakao.map.view.map

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.R
import campus.tech.kakao.map.view.search.MainActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles


class MapActivity : AppCompatActivity() {
    private val searchEditText by lazy { findViewById<EditText>(R.id.SearchEditTextInMap) }
    private val mapView by lazy { findViewById<MapView>(R.id.map_view) }
    private val bottomSheetLayout by lazy { findViewById<ConstraintLayout>(R.id.bottom_sheet_layout) }
    private val bottom_sheet_title by lazy { findViewById<TextView>(R.id.bottom_sheet_title) }
    private val bottom_sheet_address by lazy { findViewById<TextView>(R.id.bottom_sheet_address) }
    private val errorMessageTextView by lazy { findViewById<TextView>(R.id.errorMessageTextView) }
    private val bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout> by lazy { BottomSheetBehavior.from(bottomSheetLayout) }

    companion object{
        private val DEFAULT_LONGITUDE = 127.115587
        private val DEFAULT_LATITUDE = 37.406960
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        setupEditText()
        setupMapView()
    }

    override fun onResume() {
        super.onResume()
        mapView.resume() // MapView 의 resume 호출
    }

    override fun onPause() {
        super.onPause()
        mapView.pause() // MapView 의 pause 호출
    }

    private fun setupEditText() {
        searchEditText.setOnClickListener {
            val intent: Intent = Intent(this@MapActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupMapView() {
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                Log.d("jieun", "onMapDestroy")
            }

            override fun onMapError(error: Exception) {  // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출됨
                Log.d("jieun", "onMapError" + error)
                showErrorMessage(error)
            }
        }, object : KakaoMapReadyCallback() {
            val coordinates = getCoordinates()
            override fun onMapReady(kakaoMap: KakaoMap) { // 인증 후 API 가 정상적으로 실행될 때 호출됨
                Log.d("jieun", "onMapReady coordinates: " + coordinates.toString())
                if (coordinates != null) {
                    showLabel(coordinates, kakaoMap)
                    showBottomSheet(coordinates)
                    setSharedData("pref", coordinates)
//                    Log.d("jieun", "onMapReady setSharedData: " + getSharedData("pref"))
                } else{
                    hideBottomSheet()
                }
            }

            override fun getPosition(): LatLng {
//                Log.d("jieun", "getPosition coordinates: " + coordinates.toString())
                if (coordinates != null) {
                    return LatLng.from(coordinates.latitude, coordinates.longitude)
                } else{
                    return LatLng.from(DEFAULT_LATITUDE, DEFAULT_LONGITUDE)
                }

            }

        })
    }

    private fun showErrorMessage(error: Exception) {
        runOnUiThread {
            setContentView(R.layout.error_map)
            errorMessageTextView.text = "지도 인증을 실패했습니다.\n다시 시도해주세요.\n\n" + error.message
        }
    }

    private fun showLabel(
        coordinates: Coordinates,
        kakaoMap: KakaoMap
    ) {
        val labelStyles: LabelStyles = LabelStyles.from(
            LabelStyle.from(R.drawable.location_red_icon_resized).setZoomLevel(8),
            LabelStyle.from(R.drawable.location_red_icon_resized)
                .setTextStyles(32, Color.BLACK, 1, Color.GRAY).setZoomLevel(15)
        )
        val position = LatLng.from(coordinates.latitude, coordinates.longitude)
        kakaoMap.labelManager?.getLayer()?.addLabel(
            LabelOptions.from(position)
                .setStyles(labelStyles)
                .setTexts(coordinates.title)
        )
    }

    private fun hideBottomSheet() {
        runOnUiThread { bottomSheetLayout.visibility = View.GONE }
    }

    private fun showBottomSheet(coordinates: Coordinates) {
        runOnUiThread {
            bottomSheetLayout.visibility = View.VISIBLE
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            bottom_sheet_title.text = coordinates.title
            bottom_sheet_address.text = coordinates.address
        }
    }

    private fun getCoordinates(): Coordinates? {
        var coordinates = getCoordinatesByIntent()
        if(coordinates == null) {
            coordinates = getCoordinatedBySharedPreference("pref")
        }
        return coordinates

    }

    private fun getCoordinatesByIntent(): Coordinates? {
        if (intent.hasExtra("title") && intent.hasExtra("longitude")
            && intent.hasExtra("latitude") && intent.hasExtra("address")) {
            val title = intent.getStringExtra("title")
            val longitude = intent.getDoubleExtra("longitude", 0.0)
            val latitude = intent.getDoubleExtra("latitude", 0.0)
            val address = intent.getStringExtra("address").toString()
            if (title != null) {
                return Coordinates(title, longitude, latitude, address)
            } else return null
        } else return null
    }

    fun setSharedData(name: String, coordinates: Coordinates?) {
        if (coordinates != null) {
            var pref: SharedPreferences = getSharedPreferences(name, Activity.MODE_PRIVATE)
            var editor: SharedPreferences.Editor = pref.edit()
            editor.putString("longitude", coordinates.longitude.toString())
            editor.putString("latitude", coordinates.latitude.toString())
            editor.putString("title", coordinates.title.toString())
            editor.putString("address", coordinates.address.toString())
            editor.apply()
        }
    }

    fun getCoordinatedBySharedPreference(name: String): Coordinates? {
        var pref: SharedPreferences = getSharedPreferences(name, Activity.MODE_PRIVATE)
        if(pref.getString("title", "") == ""){
            return null
        }
        val title = pref.getString("title", "").toString()
        val longitude = pref.getString("longitude", "").toString().toDouble()
        val latitude = pref.getString("latitude", "").toString().toDouble()
        val address = pref.getString("address", "").toString()
        return Coordinates(title, longitude, latitude, address)
    }
}