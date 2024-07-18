package campus.tech.kakao.map.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityMainBinding
import com.kakao.sdk.common.util.Utility
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapAuthException
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.LabelTextStyle


class MainActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_PLACE_LONGITUDE = "extra_place_longitude"
        const val EXTRA_PLACE_LATITUDE = "extra_place_latitude"
        const val EXTRA_PLACE_NAME = "extra_place_name"
        const val EXTRA_PLACE_ADDRESSNAME = "extra_place_addressname"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var mapView: MapView
    private lateinit var kakaoMap: KakaoMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initKakaoMap()
        initSearchEditText()
        initMapView()

    }

    private fun initKakaoMap() {
        val keyHash = Utility.getKeyHash(this)
        Log.d("testt", keyHash)
        val key = getString(R.string.kakao_api_key) + 1
        KakaoMapSdk.init(this, key)
    }

    private fun initSearchEditText() {
        binding.searchEditText.isFocusable = false
        binding.searchEditText.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initMapView() {
        mapView = binding.mapView
        mapView.start(object : MapLifeCycleCallback() {

            override fun onMapDestroy() {
                Log.d("testt", "onMapDestory")
            }

            override fun onMapError(error: Exception) {
                val errorMessage = when ((error as? MapAuthException)?.errorCode) {
                    401 -> "API 인증에 실패"
                    499 -> "서버 통신 실패"
                    else -> "알 수 없는 오류"
                }
                val intent = Intent(this@MainActivity, ErrorActivity::class.java)
                intent.putExtra("error", errorMessage)
                startActivity(intent)
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                this@MainActivity.kakaoMap = kakaoMap
                setCameraPosition()
                setMarker()
                setBottomSheet()
            }
        })
    }

    private fun setCameraPosition() {
        val sharedPreferences = getSharedPreferences("PlacePreferences", Context.MODE_PRIVATE)
        val longitude =
            sharedPreferences.getString(EXTRA_PLACE_LONGITUDE, "127.108621")?.toDouble() ?: 0.0
        val latitude =
            sharedPreferences.getString(EXTRA_PLACE_LATITUDE, "37.402005")?.toDouble() ?: 0.0
        val cameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(latitude, longitude))
        Log.d("testt", longitude.toString())
        kakaoMap.moveCamera(cameraUpdate)
    }

    private fun setMarker() {
        val sharedPreferences = getSharedPreferences("PlacePreferences", Context.MODE_PRIVATE)
        val longitude =
            sharedPreferences.getString(EXTRA_PLACE_LONGITUDE, "127.108621")?.toDouble() ?: 0.0
        val latitude =
            sharedPreferences.getString(EXTRA_PLACE_LATITUDE, "37.402005")?.toDouble() ?: 0.0
        val placeName = sharedPreferences.getString(EXTRA_PLACE_NAME, "Unkwon")
        var styles = LabelStyles.from(
            LabelStyle.from(R.drawable.marker_128).setZoomLevel(10)
                .setTextStyles(LabelTextStyle.from(32, Color.parseColor("#000000")))
        )

        styles = kakaoMap.labelManager!!.addLabelStyles(styles!!)
        kakaoMap.labelManager!!.layer!!.addLabel(
            LabelOptions.from(LatLng.from(latitude, longitude))
                .setStyles(styles)
                .setTexts(placeName)
        )
    }

    private fun setBottomSheet() {
        val sharedPreferences = getSharedPreferences("PlacePreferences", Context.MODE_PRIVATE)
        val placeName = sharedPreferences.getString(EXTRA_PLACE_NAME, "Unknown Place").toString()
        val addressName =
            sharedPreferences.getString(EXTRA_PLACE_ADDRESSNAME, "Unknown Address").toString()

        binding.bottomSheetTitle.text = placeName
        binding.bottomSheetDescription.text = addressName
    }

    override fun onResume() {
        super.onResume()
        Log.d("testt", "onResume")
        mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        Log.d("testt", "onPause")
        mapView.pause()
    }
}

