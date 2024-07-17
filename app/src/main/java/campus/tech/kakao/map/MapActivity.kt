package campus.tech.kakao.map

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelStyles
import java.lang.Exception
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.label.Label


class MapActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var errorLayout: LinearLayout
    private lateinit var errorMessage: TextView
    private lateinit var placeNameTextView: TextView
    private lateinit var placeAddressTextView: TextView
    private lateinit var bottomSheet: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mapView = findViewById(R.id.map_view)
        errorLayout = findViewById(R.id.error_layout)
        errorMessage = findViewById(R.id.error_message)
        placeNameTextView = findViewById(R.id.place_name)
        placeAddressTextView = findViewById(R.id.place_address)
        bottomSheet = findViewById(R.id.bottomSheet)
        val inputMap = findViewById<TextView>(R.id.inputSearchMap)
        val refreshImageView = findViewById<ImageView>(R.id.refreshImageView)

        startMapView()

        refreshImageView.setOnClickListener {  // 추가된 부분
            retryMapLoad()  // 클릭 시 retryMapLoad 호출
        }

        inputMap.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    private fun startMapView() {
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                //지도 API가 정상적으로 종료될 때 호출됨
            }

            override fun onMapError(error: Exception?) {
                //지도 사용 중 에러가 발생할 때 호출됨
                error?.let { showErrorLayout(it) }
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                //인증 후 API가 정상적으로 실행될 때 호출됨
                handleIntent(kakaoMap)
                //showErrorLayout(Exception("강제 에러 호출"))
            }
        })
    }

    private fun retryMapLoad() {  // 재시도 버튼 클릭 시 호출되는 메서드
        errorLayout.visibility = View.GONE
        mapView.visibility = View.VISIBLE
        startMapView()
    }

    private fun showErrorLayout(error: Exception) {
        mapView.visibility = View.GONE
        errorLayout.visibility = View.VISIBLE
        errorMessage.text = error.toString()
    }

    private fun handleIntent(kakaoMap: KakaoMap) {
        intent?.let {
            val placeName = it.getStringExtra("place_name") ?: return
            val placeAddress = it.getStringExtra("place_address") ?: return
            val placeX = it.getStringExtra("place_x") ?: return
            val placeY = it.getStringExtra("place_y") ?: return

            placeNameTextView.text = placeName
            placeAddressTextView.text = placeAddress
            bottomSheet.visibility = View.VISIBLE

            val markerBitmap = BitmapFactory.decodeResource(resources, R.drawable.marker)
            val scaledBitmap = Bitmap.createScaledBitmap(markerBitmap, 50, 50, true)

            val labelStyle = LabelStyle.from(scaledBitmap)
            val labelStyles = LabelStyles.from(labelStyle)
            val labelOptions = LabelOptions.from(LatLng.from(placeY.toDouble(), placeX.toDouble())).setStyles(labelStyles)
            val labelLayer = kakaoMap.labelManager?.layer
            labelLayer?.addLabel(labelOptions)
            kakaoMap.moveCamera(CameraUpdateFactory.newCenterPosition(LatLng.from(placeY.toDouble(), placeX.toDouble()), 15))

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
