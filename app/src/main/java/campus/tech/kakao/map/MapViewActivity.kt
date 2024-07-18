package campus.tech.kakao.map

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import campus.tech.kakao.map.databinding.ActivityMapViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView

class MapViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMapViewBinding
    private lateinit var mapModel: MapModel
    private lateinit var mapViewModel: MapViewModel

    private lateinit var mapView: MapView
    private lateinit var searchTextview: TextView

    private lateinit var bottomSheetLayout: LinearLayout
    private lateinit var bottomSheetPlaceName: TextView
    private lateinit var bottomSheetPlaceAddr: TextView
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("mytest", "onCreate")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map_view)

        mapModel = MapModel(application as MyApplication)

        val viewModelFactory = MapViewModelFactory(application as MyApplication, mapModel)
        mapViewModel = ViewModelProvider(this, viewModelFactory)[MapViewModel::class.java]

        binding.viewModel = mapViewModel
        binding.lifecycleOwner = this

        mapView = binding.map
        searchTextview = binding.search
        bottomSheetLayout = binding.bottomView.bottomSheetLayout
        bottomSheetPlaceName = binding.bottomView.placeName
        bottomSheetPlaceAddr = binding.bottomView.placeAddress
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)

        processIntent()

        try {
            mapView.start(object : MapLifeCycleCallback() {
                override fun onMapDestroy() {
                    Log.d("KakaoMap", "카카오맵 정상종료")
                }

                override fun onMapError(exception: Exception?) {
                    Log.e("KakaoMap", "카카오맵 인증실패", exception)
                }
            }, object : KakaoMapReadyCallback() {
                override fun onMapReady(map: KakaoMap) {
                    Log.d("KakaoMap", "카카오맵 정상실행")
                }
            })
            Log.d("MapViewActivity", "mapView start called")
        } catch (e: Exception) {
            Log.e("MapViewActivity", "Exception during mapView.start", e)
        }

        searchTextview.setOnClickListener { onSearchTextViewClick() }
    }

    private fun onSearchTextViewClick() {
        startActivity(Intent(this@MapViewActivity, MainActivity::class.java))
    }

    private fun processIntent() {
        val placeName: String? = intent.getStringExtra("PLACE_NAME")
        val placeAddress: String? = intent.getStringExtra("PLACE_LOCATION")
        Log.d("mytest", "call processIntent")
        if (!placeName.isNullOrBlank() && !placeAddress.isNullOrBlank()) {
            showBottomSheet(placeName, placeAddress)
            Log.d("mytest", "Intent exist")
        } else {
            Log.d("mytest", "No Intent")
            hideBottomSheet()
        }
    }

    private fun showBottomSheet(placeName: String, placeAddr: String){
        Log.d("mytest", "call showBottomSheet")
        bottomSheetPlaceName.text = placeName
        bottomSheetPlaceAddr.text = placeAddr
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun hideBottomSheet(){
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onStart() {
        super.onStart()
        Log.d("mytest", "onStart called")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("mytest", "onReStart called")
    }

    override fun onResume() {
        super.onResume()
        Log.d("mytest", "onResume called")
        mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        Log.d("mytest", "onPause called")
        mapView.pause()
    }
}
