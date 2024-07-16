package campus.tech.kakao.map.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.domain.model.Place
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
    private lateinit var mapView : MapView
    private lateinit var searchView: SearchView
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var resPlace: Place
    private lateinit var kakaoMap: KakaoMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mapView = findViewById(R.id.mapView)
        setResultLauncher()

        mapView.start(object : MapLifeCycleCallback(){
            override fun onMapDestroy() {
            }
            override fun onMapError(p0: Exception?) {
            }
        },object :KakaoMapReadyCallback(){
            override fun onMapReady(map: KakaoMap) {
                kakaoMap = map
            }
        })

        searchView = findViewById(R.id.searchView)
        searchView.setOnClickListener {
            val intent = Intent(this, ViewActivity::class.java)
            resultLauncher.launch(intent)

        }
    }

    private fun setResultLauncher(){
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
                if(result.resultCode == Activity.RESULT_OK){
                    val placeData = result.data?.getSerializableExtra("placeData")
                    if (placeData is Place){
                        resPlace = placeData
                        val cameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(placeData.yPos.toDouble(),
                            placeData.xPos.toDouble()),15)
                        kakaoMap.moveCamera(cameraUpdate)


                        val styles = kakaoMap.labelManager?.
                        addLabelStyles(LabelStyles.from(LabelStyle.from(campus.tech.kakao.map.R.drawable.icon_location)))
                        val options: LabelOptions =
                            LabelOptions.from(LatLng.from(placeData.yPos.toDouble(),
                                placeData.xPos.toDouble()))
                                .setStyles(styles)

                        val layer = kakaoMap.labelManager?.layer
                        layer?.addLabel(options)
                    }
                }
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