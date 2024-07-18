package campus.tech.kakao.map

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import campus.tech.kakao.map.databinding.ActivityMapViewBinding
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map_view)

        mapModel = MapModel(application as MyApplication)

        val viewModelFactory = MapViewModelFactory(application as MyApplication, mapModel)
        mapViewModel = ViewModelProvider(this, viewModelFactory)[MapViewModel::class.java]

        binding.viewModel = mapViewModel
        binding.lifecycleOwner = this

        mapView = binding.map
        searchTextview = binding.search

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

    override fun onResume() {
        super.onResume()
        Log.d("MapViewActivity", "onResume called")
        mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        Log.d("MapViewActivity", "onPause called")
        mapView.pause()
    }
}
