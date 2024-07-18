package campus.tech.kakao.map.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import campus.tech.kakao.map.R
import campus.tech.kakao.map.data.Keyword
import campus.tech.kakao.map.databinding.ActivityMainBinding
import campus.tech.kakao.map.viewmodel.SearchViewModel
import campus.tech.kakao.map.viewmodel.SearchViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelLayer
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var kakaoMap: KakaoMap
    private lateinit var labelLayer: LabelLayer
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>
    private val viewModel: SearchViewModel by viewModels { SearchViewModelFactory(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("MainActivity", "onCreate called")

        initializeKakaoSdk()
        initializeMapView()
        setSearchBoxClickListener()

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        binding.bottomSheet.visibility = View.GONE

        binding.errorLayout.visibility = View.GONE

        observeViewModel()
    }

    private fun initializeKakaoSdk() {
        val apiKey = getString(R.string.kakao_api_key)
        KakaoMapSdk.init(this, apiKey)
        Log.d("MainActivity", "Kakao API Key: $apiKey")
        Log.d("MainActivity", "KakaoMapSdk initialized")
    }

    private fun initializeMapView() {
        binding.mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                Log.d("MainActivity", "onMapDestroy called")
            }

            override fun onMapError(error: Exception) {
                Log.e("MainActivity", "onMapError: ${error.message}")
                binding.errorLayout.visibility = View.VISIBLE
                binding.errorDetails.text = error.message
                binding.mapView.visibility = View.GONE
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(map: KakaoMap) {
                kakaoMap = map
                val labelManager = kakaoMap.labelManager
                if (labelManager != null) {
                    labelLayer = labelManager.layer!!
                } else {
                    Log.e("MainActivity", "LabelManager is null")
                }
                viewModel.loadLastMarkerPosition()
                Log.d("MainActivity", "Map is ready")
            }

            override fun getPosition(): LatLng {
                return LatLng.from(36.37591485731178, 127.34381616478682)
            }
        })
    }

    private fun setSearchBoxClickListener() {
        binding.searchBar.setOnClickListener {
            Log.d("MainActivity", "Search box clicked")
            val intent = Intent(this, SearchActivity::class.java)
            startActivityForResult(intent, SEARCH_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SEARCH_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.let {
                viewModel.processActivityResult(it)
            }
        }
    }

    private fun addMarker(placeName: String?, roadAddressName: String?, x: Double, y: Double) {
        if (placeName != null && roadAddressName != null) {
            val position = LatLng.from(y, x)
            val styles = kakaoMap.labelManager?.addLabelStyles(
                LabelStyles.from(
                    LabelStyle.from(R.drawable.marker).setZoomLevel(1),
                    LabelStyle.from(R.drawable.marker).setZoomLevel(1)
                )
            )

            val labelOptions = LabelOptions.from(placeName, position).apply {
                setTexts(placeName)
                setStyles(styles ?: LabelStyles.from(LabelStyle.from(R.drawable.marker)))
            }

            labelLayer.addLabel(labelOptions)

            moveCamera(position)
            updateBottomSheet(placeName, roadAddressName)
            viewModel.saveLastMarkerPosition(Keyword(0, placeName, roadAddressName, x, y))
        }
    }

    private fun moveCamera(position: LatLng) {
        kakaoMap.moveCamera(
            CameraUpdateFactory.newCenterPosition(position),
            CameraAnimation.from(10, false, false)
        )
    }

    private fun updateBottomSheet(placeName: String, roadAddressName: String) {
        binding.placeName.text = placeName
        binding.placeAddress.text = roadAddressName
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        binding.bottomSheet.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        Log.d("MainActivity", "onResume called")
        binding.mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        Log.d("MainActivity", "onPause called")
        binding.mapView.pause()
    }

    private fun observeViewModel() {
        viewModel.selectedKeyword.observe(this) { keyword ->
            addMarker(keyword.name, keyword.address, keyword.x, keyword.y)
        }
        viewModel.lastMarker.observe(this) { keyword ->
            addMarker(keyword.name, keyword.address, keyword.x, keyword.y)
        }
    }

    companion object {
        private const val SEARCH_REQUEST_CODE = 1
    }
}
