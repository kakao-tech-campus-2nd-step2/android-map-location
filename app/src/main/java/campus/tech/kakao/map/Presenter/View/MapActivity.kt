package campus.tech.kakao.map.Presenter.View

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
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
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles


class MapActivity : AppCompatActivity() {
    private lateinit var repository: PlaceRepository
    private lateinit var viewModel: MapViewModel
    private lateinit var mapView: MapView
    private lateinit var searchText: TextView
    private lateinit var bottomSheet : ConstraintLayout
    private lateinit var placeName : TextView
    private lateinit var placeAddress : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        var latLng : LatLng = LatLng.from(0.0,0.0)

        repository = (application as MyApplication).appContainer.repository
        mapView = findViewById<MapView>(R.id.mapView)
        searchText = findViewById<TextView>(R.id.search)
        bottomSheet = findViewById<ConstraintLayout>(R.id.bottomSheet)
        placeName = findViewById<TextView>(R.id.placeName)
        placeAddress = findViewById<TextView>(R.id.placeAddress)

        viewModel =
            ViewModelProvider(this, ViewModelFactory(repository))[MapViewModel::class.java]

        initViewModel()
        settingMap()
        setSearchListener()
    }

    override fun onResume() {
        super.onResume()
        mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        mapView.pause()
    }
    private fun settingMap(){
        var latLng = LatLng.from(0.0,0.0)
        var name = ""

        viewModel.currentPlace.observe(this@MapActivity){
           it?.let{
               latLng = LatLng.from(it.y,it.x)
               name = it.name

               bottomSheet.visibility = VISIBLE
               placeName.text = it.name
               placeAddress.text = it.address

               save(it.id)
           }
        }

        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
            }

            override fun onMapError(error: Exception) {
            }
        }, object : KakaoMapReadyCallback() {

            override fun onMapReady(kakaoMap: KakaoMap) {
                makeMark(kakaoMap,latLng,name)
            }

            override fun getPosition(): LatLng {
                return latLng
            }

        })
    }

    private fun makeMark(kakaoMap: KakaoMap, latLng: LatLng, name : String?) {
        val styles = kakaoMap.labelManager?.addLabelStyles(
            LabelStyles.from(
                LabelStyle.from(R.drawable.location_pin)
                .setTextStyles(LABEL_TEXT_SIZE,Color.BLACK)
                .setZoomLevel(LABEL_ZOOM_LEVEL)
            )
        )
        val options = LabelOptions.from(latLng).setStyles(styles).setTexts(name)
        val layer = kakaoMap.labelManager?.layer

        layer?.addLabel(options)
    }

    private fun initViewModel(){
            intent.extras?.getInt("id")?.let {
                viewModel.initPlace(it)

    private fun setSearchListener(){
        searchText.setOnClickListener {
            val intent = Intent(this, PlaceSearchActivity::class.java)
            startActivity(intent)
        }
    }

    companion object{
        private const val LABEL_TEXT_SIZE = 24
        private const val LABEL_ZOOM_LEVEL = 8
    }

}