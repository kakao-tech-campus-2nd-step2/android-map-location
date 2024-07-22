package campus.tech.kakao.map.ui.map

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityMapBinding
import campus.tech.kakao.map.model.Location
import campus.tech.kakao.map.ui.IntentKeys.EXTRA_MAP_ERROR_MESSAGE
import campus.tech.kakao.map.ui.IntentKeys.EXTRA_PLACE_ADDRESS
import campus.tech.kakao.map.ui.IntentKeys.EXTRA_PLACE_LATITUDE
import campus.tech.kakao.map.ui.IntentKeys.EXTRA_PLACE_LONGITUDE
import campus.tech.kakao.map.ui.IntentKeys.EXTRA_PLACE_NAME
import campus.tech.kakao.map.ui.search.SearchActivity
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.label.LabelManager
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMapBinding
    private lateinit var searchActivityResultLauncher: ActivityResultLauncher<Intent>

    private val locationViewModel: LocationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        collectLocationUpdates()
        setBottomSheet()
        startMapView()
        setSearchBoxClickListener()
        setSearchActivityResultLauncher()
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
            val longitude = data?.getDoubleExtra(EXTRA_PLACE_LONGITUDE, 0.0) ?: 0.0
            val latitude = data?.getDoubleExtra(EXTRA_PLACE_LATITUDE, 0.0) ?: 0.0

            val newLocation = Location(name, latitude, longitude, address)
            locationViewModel.saveLocation(newLocation)
            setBottomSheet()
            startMapView()
        }
    }

    /**
     * bottom sheet의 name과 address의 text를 설정하는 함수.
     *
     */
    private fun setBottomSheet() {
        locationViewModel.location.value.let { location ->
            binding.bottomSheetPlaceNameTextView.text = location.name
            binding.bottomSheetPlaceAddressTextView.text = location.address
        }
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
                startErrorActivity(error.message)
                val intent = Intent(this@MapActivity, ErrorActivity::class.java)
                intent.putExtra(EXTRA_MAP_ERROR_MESSAGE, error.message)
                startActivity(intent)
                finish()
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
                return LatLng.from(
                    locationViewModel.location.value.latitude,
                    locationViewModel.location.value.longitude
                )
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
        return LabelOptions.from(
            LatLng.from(
                locationViewModel.location.value.latitude,
                locationViewModel.location.value.longitude
            )
        )
            .setStyles(labelManager.addLabelStyles(styles))
            .setTexts(locationViewModel.location.value.name)
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
     * 에러 발생 시 ErrorActivity로 이동하는 함수.
     *
     * @param errorMessage 전달할 에러 메시지.
     */
    private fun startErrorActivity(errorMessage: String?) {
        val intent = Intent(this@MapActivity, ErrorActivity::class.java)
        intent.putExtra(EXTRA_MAP_ERROR_MESSAGE, errorMessage)
        startActivity(intent)
        finish()
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

    @Override
    override fun onDestroy() {
        locationViewModel.location.value.let { location ->
            locationViewModel.saveLocation(location)
        }
        super.onDestroy()
    }

    private fun collectLocationUpdates() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                locationViewModel.location.collectLatest { location ->
                    binding.bottomSheetPlaceNameTextView.text = location.name
                    binding.bottomSheetPlaceAddressTextView.text = location.address
                }
            }
        }
    }
}
