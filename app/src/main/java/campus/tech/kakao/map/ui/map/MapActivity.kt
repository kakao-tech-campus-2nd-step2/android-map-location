package campus.tech.kakao.map.ui.map

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.databinding.ActivityMapBinding
import campus.tech.kakao.map.ui.search.SearchActivity
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback

class MapActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeKakaoMapSdk()
        startMapView()
        setSearchBoxClickListener()
    }

    /**
     * Kakao Map SDK를 초기화하는 함수.
     */
    private fun initializeKakaoMapSdk() {
        KakaoMapSdk.init(this, BuildConfig.KAKAO_API_KEY)
    }

    /**
     * mapView를 start하기 위한 함수.
     */
    private fun startMapView() {
        binding.mapView.start(
            createMapLifeCycleCallback(),
            createKakaoMapReadyCallback(),
        )
    }

    /**
     * Search Box 클릭 리스너를 설정하는 함수.
     */
    private fun setSearchBoxClickListener() {
        binding.searchBoxLayout.setOnClickListener {
            navigateToSearchActivity()
        }
    }

    /**
     * SearchActivity로 이동하는 함수.
     */
    private fun navigateToSearchActivity() {
        val intent = Intent(this@MapActivity, SearchActivity::class.java)
        startActivity(intent)
    }

    /**
     * MapLifecycleCallback을 생성하는 함수.
     *
     * @return 생성된 MapLifeCycleCallback 객체
     */
    private fun createMapLifeCycleCallback(): MapLifeCycleCallback {
        return object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                logMapDestroy()
            }

            override fun onMapError(error: Exception) {
                logMapError(error)
            }
        }
    }

    /**
     * KakaoMapReadyCallback을 생성하는 함수.
     *
     * @return 생성된 KakaoMapReadyCallback 객체
     */
    private fun createKakaoMapReadyCallback(): KakaoMapReadyCallback {
        return object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                logMapReady()
            }

            override fun getPosition(): LatLng {
                return LatLng.from(35.230934, 129.082476) // 부산대학교 컴퓨터 공학관으로 초기 위치 설정
            }
        }
    }

    /**
     * MapDestroy시 호출되는 로깅 함수.
     */
    private fun logMapDestroy() {
        Log.d("MapActivity", "onMapDestroy called")
    }

    /**
     * MapError 발생시 호출되는 로깅 함수.
     */
    private fun logMapError(error: Exception) {
        Log.e("MapActivity", "onMapError: ${error.message}")
    }

    /**
     * Kakao 지도 API 준비가 완료되었을 때 호출되는 로깅 함수.
     */
    private fun logMapReady() {
        Log.d("MapActivity", "onMapReady called")
    }

    @Override
    public override fun onResume() {
        super.onResume()
        binding.mapView.resume()
    }

    @Override
    public override fun onPause() {
        super.onPause()
        binding.mapView.pause()
    }
}
