package campus.tech.kakao.map.presentation

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import campus.tech.kakao.map.R
import campus.tech.kakao.map.data.repository.PlaceRepositoryImpl
import campus.tech.kakao.map.data.usecase.GetLastPlaceUseCaseImpl
import campus.tech.kakao.map.data.usecase.GetSearchHistoryUseCaseImpl
import campus.tech.kakao.map.data.usecase.GetSearchPlacesUseCaseImpl
import campus.tech.kakao.map.data.usecase.RemoveSearchQueryUseCaseImpl
import campus.tech.kakao.map.data.usecase.SaveLastPlaceUseCaseImpl
import campus.tech.kakao.map.data.usecase.SaveSearchQueryUseCaseImpl
import campus.tech.kakao.map.databinding.ActivityMapBinding
import campus.tech.kakao.map.domain.model.PlaceVO
import campus.tech.kakao.map.domain.usecase.GetLastPlaceUseCase
import campus.tech.kakao.map.domain.usecase.SaveLastPlaceUseCase
import campus.tech.kakao.map.utils.ApiKeyProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraPosition
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.LabelTextBuilder


class MapActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMapBinding
    private lateinit var mapView: MapView
    private lateinit var searchBox: CardView
    private lateinit var kakaoMap: KakaoMap
    private lateinit var mapViewModel: MapViewModel
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>


    override fun onCreate(savedInstanceState: Bundle?) {
        KakaoMapSdk.init(this, ApiKeyProvider.KAKAO_API_KEY)
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindingViews()
        initializeViewModel()
        setUpSearchBox()
        setUpMap()
    }

    private fun bindingViews() {
        mapView = binding.mapView
        searchBox = binding.searchBox
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
    }

    private fun setUpSearchBox() {
        searchBox.setOnClickListener {
            startActivity(Intent(this, PlaceActivity::class.java))
        }
    }

    private fun initializeViewModel() {
        val placeRepository = PlaceRepositoryImpl(context = this)
        val factory = MapViewModelFactory(
            SaveLastPlaceUseCaseImpl(placeRepository),
            GetLastPlaceUseCaseImpl(placeRepository)
        )
        mapViewModel = ViewModelProvider(this, factory).get(MapViewModel::class.java)
    }

    private fun setUpMap() {
        // 만약 마지막으로 찍은 장소 있으면 get, 없으면 null
        var place = mapViewModel.getLastPlace() ?: null
        // intent로 받은 장소가 있으면 그 장소로 지도 시작, 없으면 place 에 저장된 장소로 지도 시작(null or LastPlace)
        place = intent.getSerializableExtra("place") as? PlaceVO ?: place

        if (place != null) {
            // place 가 null 이 아니면 해당 장소로 맵 뷰 표시
            startMap(place)
        } else {
            // place 가 null 이면(아마 첫 어플 시작) 기본 위치로 맵 뷰 표시
            startDefaultMap()
        }
    }

    private fun startMap(place: PlaceVO) {
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                Log.d("testt", "지도 종료됨")
            }

            override fun onMapError(error: Exception) {
                handleError(error)
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                this@MapActivity.kakaoMap = kakaoMap
                setCameraPosition(place.latitude, place.longitude)
                addMarker(place.latitude, place.longitude, place.placeName)
                displayBottomSheet(place)
                mapViewModel.saveLastPlace(place)
            }
        })

    }

    private fun startDefaultMap() {
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                //no-op
            }

            override fun onMapError(error: Exception) {
                handleError(error)
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                this@MapActivity.kakaoMap = kakaoMap
                setCameraPosition(37.5665, 126.9780)
            }
        })
    }


    private fun setCameraPosition(latitude: Double, longitude: Double) {
        val cameraPositionBuilder = CameraPosition.Builder()
        cameraPositionBuilder.setPosition(LatLng.from(latitude, longitude))
        cameraPositionBuilder.setZoomLevel(21)

        kakaoMap.moveCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition.from(
                    cameraPositionBuilder
                )
            ), CameraAnimation.from(500, true, true)
        )
    }

    private fun addMarker(latitude: Double, longitude: Double, text: String) {
        var styles = LabelStyles.from(
            "marker",
            LabelStyle.from(R.drawable.marker).setZoomLevel(8)
                .setTextStyles(32, Color.BLACK, 1, Color.GRAY).setZoomLevel(15)
        )

        styles = kakaoMap.labelManager!!.addLabelStyles(styles!!)

        val label = kakaoMap.labelManager!!.layer!!.addLabel(
            LabelOptions.from(LatLng.from(latitude, longitude))
                .setTexts(LabelTextBuilder().setTexts(text))
                .setStyles(styles)
        )
    }

    private fun displayBottomSheet(place: PlaceVO) {
        binding.nameTextaView.text = place.placeName
        binding.addressTextView.text = place.addressName
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun handleError(error: Exception) {
        Log.d("testt", error.message ?: "Unknown error")
        runOnUiThread {
            Thread.sleep(1000)
            val intent = Intent(this@MapActivity, ErrorActivity::class.java)
            intent.putExtra("error", error.message)
            startActivity(intent)
        }
    }
}