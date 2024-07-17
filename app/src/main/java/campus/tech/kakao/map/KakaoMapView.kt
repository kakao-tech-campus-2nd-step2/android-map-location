package campus.tech.kakao.map

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.common.util.Utility
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.RoadViewRequest
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles


class KakaoMapView : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var searchButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kakao_map_view)

        val sharedPref = getSharedPreferences("Coordinates", Context.MODE_PRIVATE)
        val xCoordinate = sharedPref.getString("xCoordinate", null)?.toDoubleOrNull()
        val yCoordinate = sharedPref.getString("yCoordinate", null)?.toDoubleOrNull()
        Log.e("SharedPreff", "X:$xCoordinate, Y:$yCoordinate")

        val defaultX = 37.402005
        val defaultY = 127.108621

        val initialX = xCoordinate ?: defaultX
        val initialY = yCoordinate ?: defaultY

        val key = getString(R.string.kakao_api_key)
        KakaoMapSdk.init(this, key)

        var keyHash = Utility.getKeyHash(this)
        Log.d("testt", keyHash)

        mapView = findViewById(R.id.mapView)
        searchButton = findViewById(R.id.searchButton)

        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                // 지도 API 가 정상적으로 종료될 때 호출됨
                Log.e("SharedPreff", "error1")
            }

            override fun onMapError(error: Exception) {
                // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출됨
                Log.e("SharedPreff", "error2")
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                // 인증 후 API 가 정상적으로 실행될 때 호출됨
                val position = LatLng.from(initialY, initialX)

                Log.e("SharedPreff", "X1:$initialX, Y1:$initialY")
                val cameraUpdate =
                    CameraUpdateFactory.newCenterPosition(position)
                kakaoMap.moveCamera(cameraUpdate, CameraAnimation.from(500, true, true))
            }
        })

        searchButton.setOnClickListener {
            Intent(this, SearchActivity::class.java).let {
                startActivity(it)
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