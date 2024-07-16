package campus.tech.kakao.map.ui.map

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityMapBinding
import campus.tech.kakao.map.ui.IntentKeys.EXTRA_PLACE_ADDRESS
import campus.tech.kakao.map.ui.IntentKeys.EXTRA_PLACE_LATITUDE
import campus.tech.kakao.map.ui.IntentKeys.EXTRA_PLACE_LONGITUDE
import campus.tech.kakao.map.ui.IntentKeys.EXTRA_PLACE_NAME
import campus.tech.kakao.map.ui.search.SearchActivity
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.label.LabelManager
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles

class MapActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMapBinding
    private lateinit var searchActivityResultLauncher: ActivityResultLauncher<Intent>
    private var markerLatitude = 35.230934
    private var markerLongitude = 129.082476
    private var markerPlaceName = "부산대 컴공관"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeKakaoMapSdk()
        startMapView()
        setSearchBoxClickListener()
        setSearchActivityResultLauncher()
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
     * 검색 액티비티 결과를 처리하기 위한 ActivityResultLauncher를 초기화하는 함수.
     */
    private fun setSearchActivityResultLauncher() {
        searchActivityResultLauncher =
            registerForActivityResult(StartActivityForResult()) { result ->
                handleSearchActivityResult(result)
            }
    }

    /**
     * 검색 액티비티의 결과를 처리하는 함수.
     *
     * @param result 검색 액티비티의 ActivityResult 객체.
     */
    private fun handleSearchActivityResult(result: ActivityResult) {
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            val name = data?.getStringExtra(EXTRA_PLACE_NAME) ?: ""
            val address = data?.getStringExtra(EXTRA_PLACE_ADDRESS) ?: ""
            val longitude = data?.getStringExtra(EXTRA_PLACE_LONGITUDE) ?: ""
            val latitude = data?.getStringExtra(EXTRA_PLACE_LATITUDE) ?: ""

            setBottomSheet(name, address)
            setMarker(name, longitude, latitude)
            startMapView()
        }
    }

    /**
     * bottom sheet의 name과 address의 text를 설정하는 함수.
     *
     * @param name 설정할 위치의 name String.
     * @param address 설정할 위치의 address String.
     */
    private fun setBottomSheet(
        name: String,
        address: String,
    ) {
        binding.bottomSheetPlaceNameTextView.text = name
        binding.bottomSheetPlaceAddressTextView.text = address
    }

    /**
     * marker의 위치를 설정하는 함수.
     *
     * @param name marker를 표시할 위치의 이름.
     * @param longitude marker를 표시할 위치의 경도 좌표.
     * @param latitude marker를 표시할 위치의 위도 좌표.
     */
    private fun setMarker(
        name: String,
        longitude: String,
        latitude: String,
    ) {
        markerPlaceName = name
        markerLongitude = longitude.toDouble()
        markerLatitude = latitude.toDouble()
    }

    /**
     * 검색 액티비티로 이동하는 함수.
     */
    private fun navigateToSearchActivity() {
        val intent = Intent(this@MapActivity, SearchActivity::class.java)
        searchActivityResultLauncher.launch(intent)
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
                val labelManager = kakaoMap.labelManager
                if (labelManager != null) {
                    addLabelsToMap(labelManager)
                }
            }

            override fun getPosition(): LatLng {
                return LatLng.from(markerLatitude, markerLongitude) // 부산대학교 컴퓨터 공학관으로 초기 위치 설정
            }
        }
    }

    /**
     * 지도에 라벨을 추가하는 함수.
     *
     * @param labelManager 라벨을 관리하는 LabelManager 객체.
     */
    private fun addLabelsToMap(labelManager: LabelManager) {
        val styles = createLabelStyles()
        val labelOptions = createLabelOptions(labelManager, styles)
        labelManager.layer?.addLabel(labelOptions)
    }

    /**
     * 라벨의 스타일을 생성하는 함수.
     *
     * @return 생성된 LabelStyles 객체
     */
    private fun createLabelStyles(): LabelStyles {
        return LabelStyles.from(
            "marker",
            LabelStyle.from(R.drawable.location_pin_red)
                .setTextStyles(28, Color.BLACK, 1, Color.BLACK)
                .setZoomLevel(8),
        )
    }

    /**
     * 라벨의 옵션을 생성하는 함수.
     *
     * @return 생성된 LabelOptions 객체
     */
    private fun createLabelOptions(
        labelManager: LabelManager,
        styles: LabelStyles,
    ): LabelOptions {
        return LabelOptions.from(LatLng.from(markerLatitude, markerLongitude))
            .setStyles(labelManager.addLabelStyles(styles))
            .setTexts(markerPlaceName)
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
