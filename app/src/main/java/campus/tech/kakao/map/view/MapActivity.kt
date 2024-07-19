package campus.tech.kakao.map.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.ui.BottomSheetFragment
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles

class MapActivity : AppCompatActivity() {
    private val mapView by lazy<MapView> { findViewById(R.id.mapView) }
    private val searchView by lazy<SearchView> { findViewById(R.id.searchView) }
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var bottomSheetFragment: BottomSheetFragment
    private lateinit var tvErrorMessage: TextView
    private lateinit var kakaoMap: KakaoMap
  
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        initializeMapView()
        initializeSearchView()
        setResultLauncher()
    }

    private fun initializeMapView() {
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {}
            override fun onMapError(error: Exception) {
                showErrorPage(error)
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(map: KakaoMap) {
                if(!isNetworkAvailable()){
                    showErrorPage(java.lang.Exception("네트워크 연결 오류"))
                }
                kakaoMap = map
                initMap()
            }
        })
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnected == true
    }

    private fun initializeSearchView() {
        searchView.setOnClickListener {
            val intent = Intent(this, ViewActivity::class.java)
            resultLauncher.launch(intent)
        }
    }

    private fun showErrorPage(error: Exception){
        setContentView(R.layout.error_page)
        tvErrorMessage = findViewById(R.id.tvErrorMessage)
        tvErrorMessage.text = "지도 인증에 실패했습니다.\n다시 시도해주세요.\n"+ error.message
    }

    private fun setResultLauncher() {
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val placeData = result.data?.getSerializableExtra("placeData") as? Place
                placeData?.let {
                    updateMapWithPlaceData(it)
                    saveLastVisitedPlace(it)
                    showBottomSheet(it)
                }
            }
        }
    }

    private fun updateMapWithPlaceData(place: Place) {
        val cameraUpdate = CameraUpdateFactory.newCenterPosition(
            LatLng.from(place.yPos.toDouble(), place.xPos.toDouble()), 15
        )
        kakaoMap.moveCamera(cameraUpdate)

        val styles = kakaoMap.labelManager?.addLabelStyles(
            LabelStyles.from(LabelStyle.from(R.drawable.icon_location))
        )
        val options = LabelOptions.from(
            LatLng.from(place.yPos.toDouble(), place.xPos.toDouble())
        ).setStyles(styles)

        val layer = kakaoMap.labelManager?.layer
        layer?.addLabel(options)
    }

    private fun showBottomSheet(place: Place) {
        bottomSheetFragment = BottomSheetFragment(place)
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }

    private fun initMap() {
        val sharedPreferences = getSharedPreferences("LastVisitedPlace", MODE_PRIVATE)
        val placeName = sharedPreferences.getString("placeName", null)
        val roadAddressName = sharedPreferences.getString("roadAddressName", null)
        val categoryName = sharedPreferences.getString("categoryName", null)
        val yPos = sharedPreferences.getString("yPos", null)
        val xPos = sharedPreferences.getString("xPos", null)

        if (placeName != null && roadAddressName != null && categoryName != null && yPos != null && xPos != null) {
            val place = Place("", placeName, roadAddressName, categoryName, xPos, yPos)
            updateMapWithPlaceData(place)
            showBottomSheet(place)
        }
    }

    private fun saveLastVisitedPlace(place: Place) {
        val sharedPreferences = getSharedPreferences("LastVisitedPlace", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("placeName", place.place)
        editor.putString("roadAddressName", place.address)
        editor.putString("categoryName", place.category)
        editor.putString("yPos", place.yPos)
        editor.putString("xPos", place.xPos)
        editor.apply()
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

