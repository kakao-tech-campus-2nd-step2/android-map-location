package campus.tech.kakao.map.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityMapBinding
import campus.tech.kakao.map.model.Location
import campus.tech.kakao.map.viewmodel.MapViewModel
import campus.tech.kakao.map.viewmodel.MapViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapAuthException
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.Label
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.LabelTextBuilder

val Context.dataStore by preferencesDataStore(name = "last_location_datastore")

class MapActivity : AppCompatActivity() {
    private lateinit var viewModel: MapViewModel
    private lateinit var binding: ActivityMapBinding
    private lateinit var searchLocationLauncher: ActivityResultLauncher<Intent>
    private lateinit var mapErrorLauncher: ActivityResultLauncher<Intent>
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    private lateinit var myKakaoMap: KakaoMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModelFactory = MapViewModelFactory(dataStore)
        viewModel = ViewModelProvider(this, viewModelFactory)[MapViewModel::class.java]

        searchLocationLauncher = createSearchLocationLauncher()
        mapErrorLauncher = createMapErrorLauncher()
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet.bottomSheet)
        setKakaoMap(binding.kakaoMapView)

        bottomSheetBehavior.state = STATE_HIDDEN
        bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)

        binding.searchBackgroundView.setOnClickListener {
            val intent = Intent(this@MapActivity, SearchLocationActivity::class.java)
            searchLocationLauncher.launch(intent)
        }
    }

    private fun createSearchLocationLauncher(): ActivityResultLauncher<Intent> {
        return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val markerLocation = if (Build.VERSION.SDK_INT >= 33) {
                    it.data?.getSerializableExtra("markerLocation", Location::class.java)
                } else {
                    it.data?.getSerializableExtra("markerLocation") as Location?
                }

                markerLocation?.let { location ->
                    setMarker(location)
                    moveMapCamera(location.latitude, location.longitude)
                    setBottomSheet(location)
                    viewModel.saveLastLocation(location.latitude, location.longitude)
                }
            }
        }
    }

    private fun createMapErrorLauncher(): ActivityResultLauncher<Intent> {
        return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            setKakaoMap(binding.kakaoMapView)
        }
    }

    private fun setKakaoMap(mapView: MapView) {
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                Log.d("MapActivity", "onMapDestroy")
            }

            override fun onMapError(e: Exception?) {
                val intent = Intent(this@MapActivity, MapErrorActivity::class.java)

                val errorDescription = getString(
                    when ((e as MapAuthException).errorCode) {
                        401 -> R.string.Error401
                        403 -> R.string.Error403
                        429 -> R.string.Error429
                        499 -> R.string.Error499
                        else -> R.string.ErrorDefault
                    }
                )
                intent.putExtra("errorDescription", errorDescription)
                intent.putExtra("errorCode", e.message)

                mapErrorLauncher.launch(intent)
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(map: KakaoMap) {
                myKakaoMap = map
                viewModel.loadLastLocation(::moveMapCamera)
            }
        })
    }

    private val markerImageStyle = LabelStyles.from(
        LabelStyle.from(R.drawable.map_marker)
            .setTextStyles(30, Color.BLACK, 5, Color.WHITE)
    )
    private var previousLabel: Label? = null

    private fun removePreviousMarker() {
        previousLabel?.let {
            myKakaoMap.labelManager?.layer?.remove(it)
            previousLabel = null
        }
    }

    private fun setMarker(location: Location) {
        myKakaoMap.labelManager?.let { labelManager ->
            removePreviousMarker()

            val style = labelManager.addLabelStyles(markerImageStyle)
            val options = LabelOptions.from(LatLng.from(location.latitude, location.longitude))
                .setStyles(style).setTexts(LabelTextBuilder().setTexts(location.name))
            previousLabel = labelManager.layer?.addLabel(options)
        }
    }

    private fun moveMapCamera(latitude: Double, longitude: Double, isAnimation: Boolean = true) {
        val cameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(latitude, longitude), 15)

        if (isAnimation) {
            myKakaoMap.moveCamera(cameraUpdate, CameraAnimation.from(500, true, true))
        } else {
            myKakaoMap.moveCamera(cameraUpdate)
        }
    }

    private fun setBottomSheet(location: Location) {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        binding.bottomSheet.bottomSheetNameTextView.text = location.name
        binding.bottomSheet.bottomSheetAddressTextView.text = location.address
    }

    private val bottomSheetCallback = object : BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == STATE_HIDDEN) removePreviousMarker()
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }
}