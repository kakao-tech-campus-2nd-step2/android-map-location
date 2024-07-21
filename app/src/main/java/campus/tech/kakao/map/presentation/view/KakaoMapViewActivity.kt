package campus.tech.kakao.map.presentation.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import campus.tech.kakao.map.R
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles


class KakaoMapViewActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var searchButton: Button
    private lateinit var placeName: TextView
    private lateinit var placeAddress: TextView
    private lateinit var persistentBottomSheet: LinearLayout

    private var kakaoMap: KakaoMap? = null
    private var xCoordinate: Double = 0.0
    private var yCoordinate: Double = 0.0
    private var name: String = ""
    private var address: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kakao_map_view)

        getData()
        initKakaoMap()

        searchButton = findViewById(R.id.searchButton)
        placeName = findViewById(R.id.placeName)
        placeAddress = findViewById(R.id.placeAddress)
        persistentBottomSheet = findViewById(R.id.persistent_bottom_sheet)
        persistentBottomSheet.visibility = View.GONE

        clickSearchButton()
    }

    private fun getData() {
        val sharedPref = getSharedPreferences("Coordinates", Context.MODE_PRIVATE)
        xCoordinate = sharedPref.getString("xCoordinate", "127.108621")?.toDoubleOrNull() ?: 127.108621
        yCoordinate = sharedPref.getString("yCoordinate", "37.402005")?.toDoubleOrNull() ?: 37.402005

        val sharedPref1 = getSharedPreferences("BottomSheet", Context.MODE_PRIVATE)
        name = sharedPref1.getString("name", "이름") ?: "이름"
        address = sharedPref1.getString("address", "주소") ?: "주소"
    }

    private fun initKakaoMap() {
        // onMapError 호출하기
        //KakaoMapSdk.init(this, "dfsfdsdsdasfds")

        val key = getString(R.string.kakao_api_key)
        KakaoMapSdk.init(this, key)

        mapView = findViewById(R.id.mapView)
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                Log.e("KakaoMapViewActivity", "Map destroyed")
            }

            override fun onMapError(error: Exception) {
                startActivity(Intent(this@KakaoMapViewActivity, MapErrorActivity::class.java))
                Log.e("KakaoMapViewActivity", "Map error: ${error.message}")
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                this@KakaoMapViewActivity.kakaoMap = kakaoMap
                mapReady()
            }
        })
    }

    private fun clickSearchButton() {
        searchButton.setOnClickListener {
            Intent(this, SearchActivity::class.java).let {
                startActivity(it)
            }
        }
    }

    private fun mapReady() {
        kakaoMap?.let { map ->
            val position = LatLng.from(yCoordinate, xCoordinate)

            val style = map.labelManager?.addLabelStyles(
                LabelStyles.from(LabelStyle.from(R.drawable.kakaomap_logo).setTextStyles(30, Color.BLUE))
            )

            val options: LabelOptions = LabelOptions.from(position).setStyles(style)

            val layer = map.labelManager?.layer
            layer?.addLabel(options)?.changeText(name)

            val cameraUpdate = CameraUpdateFactory.newCenterPosition(position)
            map.moveCamera(cameraUpdate, CameraAnimation.from(500, true, true))

            if (name == "이름") {
                persistentBottomSheet.visibility = View.GONE
                layer?.hideAllLabel()
            } else {
                placeName.text = name
                placeAddress.text = address
                layer?.showAllLabel()
                persistentBottomSheet.visibility = View.VISIBLE
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