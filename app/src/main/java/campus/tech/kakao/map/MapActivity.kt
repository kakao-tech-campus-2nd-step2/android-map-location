package campus.tech.kakao.map

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
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
    lateinit var mapView: MapView
    lateinit var startMainActivityForResult: ActivityResultLauncher<Intent>
    var map: KakaoMap? = null
    var savedLatitude: Double = 37.5642
    var savedLongitude: Double = 127.00

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val etSearch = findViewById<EditText>(R.id.etSearch)
        mapView = findViewById(R.id.map_view)
        loadData()  // 저장 위치 가져오기

        // 수정 필요 (Week4 Step 1 Feedback)
        startMainActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.let { data ->
                    val name = data.getStringExtra("name") ?: ""
                    val address = data.getStringExtra("address") ?: ""
                    val latitude = data.getStringExtra("latitude")?.toDoubleOrNull()
                    val longitude = data.getStringExtra("longitude")?.toDoubleOrNull()
                }
            }
        }

        etSearch.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startMainActivityForResult.launch(intent)
        }

        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                Log.d("KakaoMap", "onMapDestroy")
            }

            override fun onMapError(error: Exception) {
                val intent = Intent(this@MapActivity, ErrorActivity::class.java).apply{
                    putExtra("errorMessage",error.toString())
                }
                startActivity(intent)
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                Log.d("KakaoMap", "onMapReady")
                map = kakaoMap
                camera(kakaoMap, savedLatitude, savedLongitude)

                // 장소 정보 받아옴
                val name = intent.getStringExtra("name")
                val address = intent.getStringExtra("address")
                val latitude = intent.getStringExtra("latitude")?.toDouble()
                val longitude = intent.getStringExtra("longitude")?.toDouble()
                if (latitude != null && longitude != null) {
                    addMarker(kakaoMap, latitude, longitude, name.toString(), address.toString())
                }
            }
        })
    }
    override fun onResume() {
        super.onResume()
        mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        mapView.pause()
        saveData(savedLatitude.toString(), savedLongitude.toString())
    }

    // 위도와 경도 저장, 가져오기
    fun saveData(latitude: String, longitude: String){
        val pref = getSharedPreferences("pref", 0)
        val edit = pref.edit()
        edit.putString("latitude", latitude)
        edit.putString("longitude", longitude)
        edit.apply()
    }

    fun loadData() {
        val pref = getSharedPreferences("pref", 0)
        savedLatitude = pref.getString("latitude", "37.5642")?.toDouble() ?: 37.5642
        savedLongitude = pref.getString("longitude", "127.00")?.toDouble() ?: 127.00
    }

    // 지도의 어느 부분이 보이는가!
    fun camera(kakaoMap: KakaoMap, latitude: Double, longitude: Double) {
        val cameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(latitude, longitude))
        kakaoMap.moveCamera(cameraUpdate)
    }

    // 지도에 마커 추가
    fun addMarker(kakaoMap: KakaoMap, latitude: Double, longitude: Double, name: String, address: String) {
        val labelManager = kakaoMap.labelManager
        val iconAndTextStyle = LabelStyles.from(
            LabelStyle.from(R.drawable.location).setTextStyles(25, Color.BLACK)
        )
        val options = LabelOptions.from(LatLng.from(latitude, longitude))
            .setStyles(iconAndTextStyle)
        val layer = labelManager?.layer
        val label = layer?.addLabel(options)
        label?.changeText(name)

        camera(kakaoMap, latitude, longitude)

        savedLatitude = latitude
        savedLongitude = longitude

        bottomSheet(name, address)
    }

    // name과 address bottomSheet
    fun bottomSheet(name: String, address: String) {
        val bottomSheetDialog = BottomSheetDialog(this@MapActivity)
        val bottomSheetLayout = layoutInflater.inflate(R.layout.bottom_sheet,null)
        bottomSheetDialog.setContentView(bottomSheetLayout)
        val bottomName = bottomSheetLayout.findViewById<TextView>(R.id.tvBName)
        val bottomAddress = bottomSheetLayout.findViewById<TextView>(R.id.tvBAddress)
        bottomName.text= name
        bottomAddress.text = address
        bottomSheetDialog.show()
    }
}
