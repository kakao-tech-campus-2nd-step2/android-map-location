package campus.tech.kakao.map

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.label.LabelLayer
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles


class MapActivity : AppCompatActivity() {
    private lateinit var mapView : MapView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val inputSpace = findViewById<TextView>(R.id.inputSpace)
        mapView = findViewById<MapView>(R.id.mapView)

        inputSpace.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        val x: Double? = intent.extras?.getDouble("x")
        val y: Double? = intent.extras?.getDouble("y")
        val name: String? = intent.extras?.getString("name")

        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                // 지도 API 가 정상적으로 종료될 때 호출됨
            }

            override fun onMapError(error: Exception?) {
                // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출됨
            }
        }, object : KakaoMapReadyCallback() {

            override fun getPosition(): LatLng {
                // 지도 시작 시 위치 좌표를 설정
                return LatLng.from(y ?: 37.406960 , x ?: 127.115587)
            }

            override fun onMapReady(kakaoMap: KakaoMap) {
                // 인증 후 API 가 정상적으로 실행될 때 호출됨
                if(x != null && y != null && name != null) {
                    showLabel(kakaoMap, x, y, name)
                }
            }
        })

        //val keyHash = Utility.getKeyHash(this)
        //Log.d("uin", keyHash)
    }

    override fun onResume() {
        super.onResume()
        mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        mapView.pause()
    }

    fun showLabel(kakaoMap: KakaoMap, x : Double, y : Double, name : String) {
        // LabelStyles 생성하기
        val styles: LabelStyles? = kakaoMap.labelManager?.addLabelStyles(
            LabelStyles.from(
                LabelStyle.from(R.mipmap.ic_place)
                    .setTextStyles(27, R.color.black, 1, R.color.black)
                    .setApplyDpScale(false)))

        // LabelLayer 가져오기 (또는 커스텀 Layer 생성)
        val layer: LabelLayer? = kakaoMap.labelManager?.layer

        // LabelOptions 생성하기
        val options = LabelOptions.from(LatLng.from(y, x))
            .setStyles(styles).setTexts(name)

        // LabelLayer 에 LabelOptions 을 넣어 Label 생성하기
        val label = layer?.addLabel(options)
    }
}