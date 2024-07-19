package campus.tech.kakao.map.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.ViewModelProvider
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.R
import campus.tech.kakao.map.model.PlaceInfo
import campus.tech.kakao.map.viewmodel.MapViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelLayer
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles

class MapActivity : AppCompatActivity() {
    private lateinit var mapView: MapView
    private lateinit var mapViewModel: MapViewModel
    private lateinit var searchFloatingBtn: ExtendedFloatingActionButton
    private lateinit var clickedPlaceNameView: TextView
    private lateinit var clickedPlaceAddressView: TextView
    private lateinit var clickedPlaceView: LinearLayoutCompat
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var errorView: ErrorView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        KakaoMapSdk.init(this, BuildConfig.KAKAO_APP_KEY)
        mapViewModel = ViewModelProvider(this)[MapViewModel::class.java]

        initView()
        setListeners()
        observeViewModel()
        initializeMap()
    }

    override fun onResume() {
        super.onResume()
        mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        mapView.pause()
        mapViewModel.saveLastPosition()
    }

    private fun initView() {
        mapView = findViewById(R.id.mapView)
        searchFloatingBtn = findViewById(R.id.searchFloatingBtn)
        clickedPlaceNameView = findViewById(R.id.clickedPlaceName)
        clickedPlaceAddressView = findViewById(R.id.clickedPlaceAddress)
        clickedPlaceView = findViewById(R.id.clickedPlaceView)
        bottomSheetBehavior = BottomSheetBehavior.from(clickedPlaceView)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        errorView = findViewById(R.id.errorView)
    }

    private fun setListeners() {
        searchFloatingBtn.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeViewModel() {
        mapViewModel.pos.observe(this) { pos ->
            pos?.let {
                moveClickedPlace(it.latitude, it.longitude)
            }
        }

        mapViewModel.clickedPlaceInfo.observe(this) { placeInfo ->
            placeInfo?.let {
                showClickedPlaceInfo(it.place_name, it.road_address_name)
                showLabel(it.y.toDouble(), it.x.toDouble())
            }
        }
    }

    private fun initializeMap() {
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                Log.d("KakaoMap", "onMapDestroy")
            }

            override fun onMapError(error: Exception) {
                Log.e("KakaoMap", "onMapError: ", error)
                showErrorView(error.message ?: "Unknown error")
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(map: KakaoMap) {
                mapViewModel.setKakaoMap(map)
                mapViewModel.getLastPosition()
                handleIntent()
            }
        })
    }

    private fun handleIntent() {
        val clickedPlaceInfo = intent.getParcelableExtra<PlaceInfo>("placeInfo")
        clickedPlaceInfo?.let {
            mapViewModel.setClickedPlaceInfo(it)
        }
    }

    private fun showClickedPlaceInfo(clickedPlaceName: String?, clickedPlaceAddress: String?) {
        clickedPlaceNameView.text = clickedPlaceName
        clickedPlaceAddressView.text = clickedPlaceAddress
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun showLabel(latitude: Double, longitude: Double) {
        val kakaoMap = mapViewModel.kakaoMap.value ?: return
        val styles: LabelStyles? = kakaoMap.labelManager
            ?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.pink_marker)))
        val options: LabelOptions =
            LabelOptions.from(LatLng.from(latitude, longitude)).setStyles(styles)
        val layer: LabelLayer? = kakaoMap.labelManager?.layer
        layer?.addLabel(options)
    }

    private fun moveClickedPlace(latitude: Double, longitude: Double) {
        val kakaoMap = mapViewModel.kakaoMap.value ?: return
        kakaoMap.moveCamera(CameraUpdateFactory.newCenterPosition(LatLng.from(latitude, longitude)))
    }

    private fun showErrorView(error: String) {
        errorView.showError(error)
        mapView.visibility = View.GONE
    }


}
