package campus.tech.kakao.map

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDragHandleView
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
    private val TAG = "KAKAOMAP"
    private lateinit var kakaoMapView: MapView
    private lateinit var searchBox: TextView
    private lateinit var infoSheetLayout: LinearLayout
    private lateinit var infoSheetName: TextView
    private lateinit var infoSheetAddress: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        kakaoMapView = findViewById(R.id.kakao_map_view)
        searchBox = findViewById(R.id.search_box)
        infoSheetLayout = findViewById(R.id.info_sheet)
        infoSheetName = findViewById(R.id.info_sheet_name)
        infoSheetAddress = findViewById(R.id.info_sheet_address)

        kakaoMapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
            }

            override fun onMapError(exception: Exception?) {
            }

        }, object : KakaoMapReadyCallback(){
            override fun onMapReady(kakaoMap: KakaoMap) {
                Log.d(TAG, "onMapReady")
                val targetLocation =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        intent?.extras?.getSerializable(Location.LOCATION, Location::class.java)
                    } else {
                        intent?.extras?.getSerializable(Location.LOCATION) as Location?
                    }

                targetLocation?.let {
                    val cameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(targetLocation.y, targetLocation.x))
                    kakaoMap.moveCamera(cameraUpdate)
                    val labelManager = kakaoMap.labelManager
                    labelManager?.let {
                        val style = labelManager.addLabelStyles(
                            LabelStyles.from(LabelStyle.from(R.drawable.location_label)
                                .setTextStyles(30, Color.BLACK))
                        )
                        labelManager.layer?.addLabel(
                            LabelOptions.from(LatLng.from(targetLocation.y, targetLocation.x))
                                .setStyles(style)
                                .setTexts(targetLocation.name)
                        )
                    }
                    infoSheetLayout.isVisible = true
                    infoSheetName.text = targetLocation.name
                    infoSheetAddress.text = targetLocation.address
                }
            }
        })

        searchBox.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        kakaoMapView.resume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        kakaoMapView.pause()
        Log.d("KAKAOMAP","onPause")
    }

}