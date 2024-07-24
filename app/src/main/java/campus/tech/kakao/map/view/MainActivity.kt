package campus.tech.kakao.map.view

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.viewmodel.MyViewModel
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityMainBinding
import campus.tech.kakao.map.model.repository.MyRepository
import campus.tech.kakao.map.util.BottomSheetManager
import campus.tech.kakao.map.viewmodel.MyViewModelFactory
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MyViewModel by viewModels {
        MyViewModelFactory(this, MyRepository(applicationContext))
    }
    private var mapView: MapView? = null
    private var kakaoMap: KakaoMap? = null
    private val KAKAO_LATITUDE: Double = 37.39571538711179
    private val KAKAO_LONGITUDE: Double = 127.11051285266876
    private val bottomSheet: LinearLayout by lazy { findViewById<LinearLayout>(R.id.bottom_sheet) }

    private var name: String = "kakao"
    private var address: String = "주소"
    private var latitude: Double = 37.39571538711179
    private var longitude: Double = 127.11051285266876

    private var cameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(latitude, longitude))
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel

        val appKey = BuildConfig.KAKAO_API_KEY
        KakaoMapSdk.init(this, appKey)

        sharedPreferences = getSharedPreferences("PlacePreferences", MODE_PRIVATE)

        //SearchPlaceActivity로 이동
        viewModel.isIntent.observe(this, Observer {
            if (it) {
                val intent = Intent(this@MainActivity, SearchPlaceActivity::class.java)
                startActivity(intent)
            }
        })

        //sharedPreference에서 name, address, longitude, latitude 받아서 변수에 저장하기
        getSharedPreferences()
        //bottomSheet 초기화
        val bottomSheetManager = BottomSheetManager(this, bottomSheet)
        bottomSheetManager.setBottomSheetText(name, address)

        mapView = binding.mapView
        mapView?.start(
            object : MapLifeCycleCallback() {
                override fun onMapDestroy() {
                    // 지도 API가 정상적으로 종료될 때 호출
                    Log.d("KakaoMap", "onMapDestroy: ")
                }

                override fun onMapError(error: Exception) {
                    // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출
                    Log.d("KakaoMap", "onMapError")
                    Log.e("KakaoMap", "onMapError: ", error)
                    val intent = Intent(this@MainActivity, MapErrorActivity::class.java)
                    intent.putExtra("error", error.toString().substring(20))
                    startActivity(intent)
                }
            },
            object : KakaoMapReadyCallback() {
                override fun onMapReady(map: KakaoMap) {
                    // 정상적으로 인증이 완료되었을 때 호출
                    // KakaoMap 객체를 얻어 옵니다.
                    kakaoMap = map
                    //카메라 업데이트
                    updateCamera()
                    addMarker(map, latitude, longitude, name)
                }
            }
        )  //mapView.start
    }   //onCreate

    override fun onResume() {
        super.onResume()
        mapView?.resume()
        Log.d("KakaoMap", "onMapResume")
        Log.d("KakaoMap", "kakaoMap : $kakaoMap")
        getSharedPreferences()
        updateCamera()
        addMarker(kakaoMap, latitude, longitude, name)
        BottomSheetManager(this, bottomSheet).setBottomSheetText(name, address)
    }

    override fun onPause() {
        super.onPause()
        mapView?.pause()
        Log.d("KakaoMap", "onMapPause")
    }

    private fun updateCamera() {
        cameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(latitude, longitude))
        kakaoMap?.moveCamera(cameraUpdate)
    }

    private fun getSharedPreferences() {
        name = sharedPreferences.getString("name", "") ?: ""
        address = sharedPreferences.getString("address", "") ?: ""
        longitude =
            sharedPreferences.getString("longitude", "0.0")?.toDoubleOrNull() ?: KAKAO_LONGITUDE
        latitude =
            sharedPreferences.getString("latitude", "0.0")?.toDoubleOrNull() ?: KAKAO_LATITUDE
    }

    private fun addMarker(kakaoMap: KakaoMap?, latitude: Double, longitude: Double, name: String) {
        //에러 로그가 필요 없다면 이렇게
        val labelManager = kakaoMap?.labelManager ?: return
        val iconAndTextStyle = LabelStyles.from(
            LabelStyle.from(R.drawable.location) // 이미지 리소스 확인
                .setTextStyles(25, Color.BLACK) // 텍스트 스타일
        )
        val options = LabelOptions.from(LatLng.from(latitude, longitude))
            .setStyles(iconAndTextStyle)
        val layer = labelManager.layer
        if (layer != null) {
            val label = layer.addLabel(options)
            label.changeText(name)
        } else {
            Log.e("AddMarker", "Layer is null")
        }
    }
}
