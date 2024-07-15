package campus.tech.kakao.map

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import java.lang.Exception

class MapActivity : AppCompatActivity() {
    private lateinit var kakaoMap: MapView
    private lateinit var searchBox: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        kakaoMap = findViewById(R.id.kakao_map)
        searchBox = findViewById(R.id.search_box)

        kakaoMap.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
            }

            override fun onMapError(p0: Exception?) {
            }

        }, object : KakaoMapReadyCallback(){
            override fun onMapReady(p0: KakaoMap) {
            }
        })

        searchBox.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        kakaoMap.resume()
    }

    override fun onPause() {
        super.onPause()
        kakaoMap.pause()
    }

}