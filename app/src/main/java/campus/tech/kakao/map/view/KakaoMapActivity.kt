package campus.tech.kakao.map.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityKakaoMapBinding
import campus.tech.kakao.map.model.Place
import campus.tech.kakao.map.repository.LastPositionRepository
import campus.tech.kakao.map.viewmodel.KakaoMapViewModel
import campus.tech.kakao.map.viewmodel.KakaoMapViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior
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
import java.lang.Exception

class KakaoMapActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKakaoMapBinding
    private lateinit var viewModel: KakaoMapViewModel
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var kakaoMap: KakaoMap
    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKakaoMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val lastPositionRepository = LastPositionRepository(this)
        val viewModelProviderFactory = KakaoMapViewModelFactory(lastPositionRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[KakaoMapViewModel::class.java]

        setUpKakaoMap()
        getSearchResult()
        gotoSearchWindowBtnListener()
        kakaoMapReloadListener()
    }

    override fun onResume() {
        super.onResume()
        mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        mapView.pause()
    }

    override fun onDestroy() {
        super.onDestroy()

        val position = kakaoMap.cameraPosition?.position
        if (position != null) {
            viewModel.saveLastPosition(position)
        }
    }

    fun setUpKakaoMap() {
        KakaoMapSdk.init(this, BuildConfig.KAKAO_API_KEY)
        mapView = binding.mapView
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {

            }

            override fun onMapError(error: Exception?) {
                binding.kakaomapErrDescription.text = error?.message.toString()
                showView(binding.kakaomapErr, true)
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                this@KakaoMapActivity.kakaoMap = kakaoMap

                val position = viewModel.lastPosition.value
                if (position != null) {
                    moveCameraPosition(position)
                }
            }
        })
    }

    fun kakaoMapReloadListener() {
        binding.reload.setOnClickListener {
            showView(binding.kakaomapErr, false)
            setUpKakaoMap()
        }
    }

    fun getSearchResult() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    val place = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        it.data?.getParcelableExtra(
                            ActivityKeys.INTENT_PLACE,
                            Place::class.java
                        )
                    } else {
                        it.data?.getParcelableExtra<Place>(ActivityKeys.INTENT_PLACE)
                    }
                    if (place != null) {
                        displayPlaceOnKakaoMap(place)
                    }
                }
            }
    }

    fun displayPlaceOnKakaoMap(place: Place) {
        val position = LatLng.from(place.getLat(), place.getLng())
        displayPlaceInMarker(position)
        moveCameraPosition(position)
        displayPlaceInfoBottomSheet(place)
    }

    fun displayPlaceInMarker(position: LatLng) {
        kakaoMap.labelManager?.clearAll()

        val newStyles = LabelStyles.from(LabelStyle.from(R.drawable.marker))

        val labelStyles = kakaoMap.labelManager?.addLabelStyles(newStyles)

        val options = LabelOptions
            .from(position)
            .setStyles(labelStyles)

        kakaoMap.labelManager?.layer?.addLabel(options)
    }

    fun moveCameraPosition(position: LatLng) {
        val cameraUpdate = CameraUpdateFactory.newCenterPosition(position)
        kakaoMap.moveCamera(cameraUpdate)
    }

    fun displayPlaceInfoBottomSheet(place: Place) {
        val bottomSheet = binding.placeInfoBottomSheet
        bottomSheet.placeName.text = place.place_name
        bottomSheet.addressName.text = place.address_name

        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet.root)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    fun gotoSearchWindowBtnListener() {
        binding.gotoSearchWindow.setOnClickListener {
            val intent = Intent(this, SearchWindowActivity::class.java)
            activityResultLauncher.launch(intent)
        }
    }

    private fun showView(view: View, isShow: Boolean) {
        view.visibility = if (isShow) View.VISIBLE else View.GONE
    }
}

