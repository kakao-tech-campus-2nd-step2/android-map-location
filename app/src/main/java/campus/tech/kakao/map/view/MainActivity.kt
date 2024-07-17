package campus.tech.kakao.map.view

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.kakao.vectormap.label.LabelManager
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.shape.MapPoints


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mapView: MapView
    private lateinit var kakaoMap: KakaoMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initKakaoMap()

        binding.searchEditText.isFocusable = false
        binding.searchEditText.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            setCameraPositionLauncher.launch(intent)
        }
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

    private fun initKakaoMap() {
        val keyHash = Utility.getKeyHash(this)
        Log.d("testt", keyHash)
        val key = getString(R.string.kakao_api_key)
        KakaoMapSdk.init(this, key)

        mapView = binding.mapView
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                Log.d("testt", "onMapDestory")
            }
            override fun onMapError(error: Exception) {
                Toast.makeText(
                    this@MainActivity,
                    when ((error as MapAuthException).errorCode) {
                        401 -> "API 인증에 실패"
                        499 -> "서버 통신 실패"
                        else -> "알 수 없는 오류"
                    },
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                this@MainActivity.kakaoMap = kakaoMap
            }
        })
    }

    private val setCameraPositionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        setCameraPosition(result)
    }

    private fun setCameraPosition(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val longitude = data?.getStringExtra(EXTRA_PLACE_LONGITUDE)?.toDouble() ?: 0.0
            val latitude = data?.getStringExtra(EXTRA_PLACE_LATITUDE)?.toDouble() ?: 0.0
            val cameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(latitude, longitude))
            kakaoMap.moveCamera(cameraUpdate)
            addMarker(latitude, longitude) // 카메라 위치를 업데이트한 후 마커 추가
        }
    }

    private fun addMarker(latitude: Double, longitude: Double) {
        var styles = LabelStyles.from(
            "marker",
            LabelStyle.from(R.drawable.marker).setZoomLevel(8)
                .setTextStyles(32, Color.BLACK, 1, Color.GRAY).setZoomLevel(15)
                .setTextStyles(32, Color.BLACK, 1, Color.GRAY).setZoomLevel(15)
        )

        styles = kakaoMap.labelManager!!.addLabelStyles(styles!!)

        kakaoMap.labelManager!!.layer!!.addLabel(
            LabelOptions.from(LatLng.from(latitude, longitude))
                .setStyles(styles)
        )
    }

    companion object {
        const val EXTRA_PLACE_LONGITUDE = "extra_place_longitude"
        const val EXTRA_PLACE_LATITUDE = "extra_place_latitude"
    }

}

