package campus.tech.kakao.map.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import campus.tech.kakao.map.databinding.ActivityMapBinding
import campus.tech.kakao.map.utils.ApiKeyProvider
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.MapViewInfo

class MapActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMapBinding
    private lateinit var mapView: MapView
    private lateinit var searchBox: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        KakaoMapSdk.init(this, ApiKeyProvider.KAKAO_API_KEY)
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mapView = binding.mapView
        searchBox = binding.searchBox


        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                // 지도 API가 정상적으로 종료될 때 호출됨
                Log.d("testt", "지도 종료됨")
            }

            override fun onMapError(error: Exception) {
                // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출됨
                Log.d("testt", "맵 에러 발생 ${error.message}")
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                // 인증 후 API가 정상적으로 실행될 때 호출됨
                Log.d("testt", "맵 준비됐지롱")

            }

            override fun getPosition(): LatLng {
                return LatLng.from(37.402056, 127.108212)
            }

            override fun getZoomLevel(): Int {
                return 20
            }

        })
        setUpSearchBox()
    }

    private fun setUpSearchBox() {
        searchBox.setOnClickListener {
            startActivity(Intent(this, PlaceActivity::class.java))
        }
    }
}