package campus.tech.kakao.map.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import campus.tech.kakao.map.Application
import campus.tech.kakao.map.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles

class HomeMapActivity : AppCompatActivity() {
    private lateinit var mapView: MapView
    private lateinit var searchBar: EditText
    private val bottomSheet: ConstraintLayout by lazy { findViewById(R.id.bottomSheet) }
    private lateinit var bottomBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var placeNameTextView: TextView
    private lateinit var placeAddressTextView: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_map)

        val name = intent.getStringExtra("name")
        val address = intent.getStringExtra("address")
        val latitude = intent.getStringExtra("latitude")?.toDouble()
        val longitude = intent.getStringExtra("longitude")?.toDouble()

        mapView = findViewById(R.id.mapView)
        placeNameTextView = findViewById(R.id.placeName)
        placeAddressTextView = findViewById(R.id.placeAddress)

        bottomBehavior = BottomSheetBehavior.from(bottomSheet)
        val intentError = Intent(this, MapErrorActivity::class.java)

        //KaKao Map UI에 띄우기
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
            }

            override fun onMapError(p0: Exception?) {
                intentError.putExtra("errorMessage",p0.toString())
                startActivity(intentError)
            }

        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(p0: KakaoMap) {
              
                // 라벨 생성
                if (latitude != null && longitude != null) {
                    p0.labelManager
                    val style =
                        p0.labelManager?.addLabelStyles(
                            LabelStyles.from(
                                LabelStyle.from(R.drawable.map_label).setTextStyles(
                                    35,
                                    Color.BLACK, 1, Color.RED
                                )
                            )
                        )
                    val options =
                        LabelOptions.from(LatLng.from(latitude, longitude)).setStyles(style)
                            .setTexts(name)
                    val layer = p0.labelManager?.layer
                    layer?.addLabel(options)
                }
            }

            // 지도 시작 시 위치 좌표 설정
            override fun getPosition(): LatLng {
                val savedLatitude = Application.prefs.getString("latitude", null).toDoubleOrNull()
                val savedLongitude =
                    Application.prefs.getString("longitude", null).toDoubleOrNull()

                return if (latitude != null && longitude != null) {
                    LatLng.from(latitude, longitude)
                } else if (savedLatitude != null && savedLongitude != null) {
                    LatLng.from(savedLatitude, savedLongitude)
                } else {
                    super.getPosition()
                }
            }
        })

        if (latitude != null && longitude != null) {
            placeNameTextView.text = name
            placeAddressTextView.text = address
            bottomBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        } else {
            bottomBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        searchBar = findViewById(R.id.search_home)
        searchBar.setOnClickListener {
            val intent = Intent(this, DataSearchActivity::class.java)
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
        intent.getStringExtra("latitude")?.let { Application.prefs.setString("latitude", it) }
        intent.getStringExtra("longitude")?.let { Application.prefs.setString("longitude", it) }
    }
}