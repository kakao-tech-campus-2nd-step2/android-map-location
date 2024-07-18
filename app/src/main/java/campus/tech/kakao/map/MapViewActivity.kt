package campus.tech.kakao.map

import android.content.Intent
import android.graphics.Color
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
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles

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

        val placeName = intent.getStringExtra("PLACE_NAME")
        val placeAddr = intent.getStringExtra("PLACE_LOCATION")
        val placeX = intent.getStringExtra("PLACE_X")
        val placeY = intent.getStringExtra("PLACE_Y")

        processBottomSheet(placeName, placeAddr)

        try {
            mapView.start(object : MapLifeCycleCallback() {
                override fun onMapDestroy() {
                    Log.d("KakaoMap", "카카오맵 정상종료")
                }

                override fun onMapError(exception: Exception?) {
                    val errorMsg = extractErrorMsg(exception.toString())
                    val intent = Intent(this@MapViewActivity, ErrorActivity::class.java)
                    intent.putExtra("ERROR", errorMsg)
                    startActivity(intent)
                    Log.d("KakaoMap", errorMsg.toString())
                }
            }, object : KakaoMapReadyCallback() {
                override fun onMapReady(map: KakaoMap) {
                    Log.d("KakaoMap", "카카오맵 정상실행")
                    val position = LatLng.from(placeY!!.toDouble(), placeX!!.toDouble())
                    // 라벨표시
                    val style = map.labelManager?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.flag).setTextStyles(40, Color.BLACK)))
                    val options: LabelOptions = LabelOptions.from(position)
                        .setStyles(style)
                    val layer = map.labelManager?.layer
                    val label = layer?.addLabel(options)
                    label?.changeText(placeName)

                    //카메라 이동
                    val cameraUpdate = CameraUpdateFactory.newCenterPosition(position)
                    map.moveCamera(cameraUpdate, CameraAnimation.from(500, true, true))
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

    private fun processBottomSheet(placeName: String?, placeAddr: String?) {
        if (!placeName.isNullOrEmpty() && !placeAddr.isNullOrEmpty()) {
            showBottomSheet(placeName, placeAddr)
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

    private fun extractErrorMsg(fullMsg: String): String {
        val parts = fullMsg.split(": ", limit = 2)
        return if (parts.size > 1) parts[1] else ""
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
