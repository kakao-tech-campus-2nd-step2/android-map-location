package campus.tech.kakao.map

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import campus.tech.kakao.map.databinding.ActivityMainBinding
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var mapView: MapView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        gotoSearchWindowBtnListener()
        displayKakaoMap()
    }

    override fun onResume() {
        super.onResume()
        mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        mapView.pause()
    }

    fun gotoSearchWindowBtnListener(){
        binding.gotoSearchWindow.setOnClickListener {
            val intent = Intent(this, SearchWindowActivity::class.java)
            startActivity(intent)
        }
    }

    fun displayKakaoMap(){
        KakaoMapSdk.init(this, BuildConfig.KAKAO_API_KEY)
        mapView = binding.mapView
        mapView.start(object: MapLifeCycleCallback() {
            override fun onMapDestroy() {

            }

            override fun onMapError(p0: Exception?) {

            }
        }, object: KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {

            }
        })
    }
}

