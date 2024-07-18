package campus.tech.kakao.map.view.map

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.R
import campus.tech.kakao.map.view.search.MainActivity
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.label.LabelManager
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles


class MapActivity : AppCompatActivity() {
    private lateinit var searchEditText: EditText
    private lateinit var mapView: MapView
    companion object{
        private val DEFAULT_LONGITUDE = 127.115587
        private val DEFAULT_LATITUDE = 37.406960
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        initViews()
        setupEditText()
        setupMapView()
        Log.d("jieun", "MapActivity onCreate"+Thread.currentThread())

    }

    override fun onStart() {
        super.onStart()
        Log.d("jieun", "onStart")
    }

    private fun initViews() {
        searchEditText = findViewById(R.id.SearchEditTextInMap)
        mapView = findViewById(R.id.map_view)
    }

    private fun setupEditText() {
        searchEditText.setOnClickListener {
            val intent: Intent = Intent(this@MapActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupMapView() {
        KakaoMapSdk.init(this, BuildConfig.KAKAO_API_KEY);
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                // 지도 API 가 정상적으로 종료될 때 호출됨
                Log.d("jieun", "onMapDestroy")
            }

            override fun onMapError(error: Exception) {
                // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출됨
                Log.d("jieun", "onMapError" + error)
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                // 인증 후 API 가 정상적으로 실행될 때 호출됨
                val coordinates = getCoordinates()
                Log.d("jieun", "onMapReady coordinates: " + coordinates.toString())
                if (coordinates != null) {
                    val labelStyles: LabelStyles = LabelStyles.from(
                        LabelStyle.from(R.drawable.location_red_icon_resized).setZoomLevel(8),
                        LabelStyle.from(R.drawable.location_red_icon_resized).setTextStyles(32, Color.BLACK, 1, Color.GRAY).setZoomLevel(15)
                    )
                    val position = LatLng.from(coordinates.latitude, coordinates.longitude)
                    kakaoMap.labelManager?.getLayer()?.addLabel(
                        LabelOptions.from(position)
                        .setStyles(labelStyles)
                        .setTexts(coordinates.title)
                    )
                }
            }

            override fun getPosition(): LatLng {
                val coordinates = getCoordinates()
                Log.d("jieun", "getPosition coordinates: " + coordinates.toString())

                if (coordinates != null) {
                    return LatLng.from(coordinates.latitude, coordinates.longitude)
                }
                return LatLng.from(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
            }

        })
    }
    private fun getCoordinates(): Coordinates? {
        if(intent == null) return null
        val title = intent.getStringExtra("title")
        val longitudeString = intent.getStringExtra("longitude")
        val latitudeString = intent.getStringExtra("latitude")

        if (title!=null && longitudeString != null && latitudeString != null) {
            val longitude = longitudeString.toDouble()
            val latitude = latitudeString.toDouble()
            val coordinates = Coordinates(title, longitude, latitude)
            return coordinates
        }
        return null
    }

    override fun onResume() {
        super.onResume()
        mapView.resume() // MapView 의 resume 호출
    }

    override fun onPause() {
        super.onPause()
        Log.d("jieun", "onPause")
        mapView.pause() // MapView 의 pause 호출
    }

}