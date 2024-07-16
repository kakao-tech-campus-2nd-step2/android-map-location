package campus.tech.kakao.map.presentation

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityMapBinding
import campus.tech.kakao.map.domain.model.PlaceVO
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
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>


    override fun onCreate(savedInstanceState: Bundle?) {
        KakaoMapSdk.init(this, ApiKeyProvider.KAKAO_API_KEY)
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindingViews()
        setUpSearchBox()

        val place = intent.getSerializableExtra("place") as? PlaceVO
        if (place != null) {
            startMap(place)
        } else {
            startDefaultMap()
        }
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

    private fun startMap(place: PlaceVO) {
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                Log.d("testt", "지도 종료됨")
            }

            override fun onMapError(error: Exception) {
                Log.d("testt", "맵 에러 발생 ${error.message}")
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                this@MapActivity.kakaoMap = kakaoMap
                setCameraPosition(place.latitude, place.longitude)
                addMarker(place.latitude, place.longitude, place.placeName)
                displayBottomSheet(place)
            }
        })

    }

    private fun startDefaultMap() {
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                //no-op
            }

            override fun onMapError(p0: java.lang.Exception?) {
                // no-op
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
}