package campus.tech.kakao.map.Presenter.View

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import campus.tech.kakao.map.Base.ViewModelFactory
import campus.tech.kakao.map.Domain.PlaceRepository
import campus.tech.kakao.map.MyApplication
import campus.tech.kakao.map.Presenter.ViewModel.MapViewModel
import campus.tech.kakao.map.R
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView


class MapActivity : AppCompatActivity() {
    private lateinit var repository: PlaceRepository
    private lateinit var viewModel: MapViewModel
    private lateinit var mapView: MapView
    private lateinit var searchText: TextView
    private lateinit var latLng : LatLng
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        repository = (application as MyApplication).appContainer.repository
        mapView = findViewById<MapView>(R.id.mapView)
        searchText = findViewById<TextView>(R.id.search)
        viewModel =
            ViewModelProvider(this, ViewModelFactory(repository))[MapViewModel::class.java]

        intent.extras?.getInt("id")?.let {
            viewModel.initPlace(it)
        }

        viewModel.currentPlace.observe(this@MapActivity){
            latLng = LatLng.from(it?.y ?: 0.0 ,it?.x ?: 0.0)
        }

        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
            }

            override fun onMapError(error: Exception) {
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {

            }

            override fun getPosition(): LatLng {
                return latLng
            }

        })


        searchText.setOnClickListener {
            val intent = Intent(this, PlaceSearchActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        mapView.pause()
    }
}