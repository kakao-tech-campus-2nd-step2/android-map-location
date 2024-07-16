package campus.tech.kakao.map.view.map

import android.content.Intent
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
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView


class MapActivity : AppCompatActivity() {
    private lateinit var searchEditText: EditText
    private lateinit var mapView: MapView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        initViews()
        setupEditText()
        setupMapView()
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
                Log.d("jieun", "onMapReady")
            }
        })
    }
}