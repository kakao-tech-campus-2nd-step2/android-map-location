package campus.tech.kakao.map.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import campus.tech.kakao.map.R
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.MapLifeCycleCallback
import java.lang.Exception

class HomeMapActivity : AppCompatActivity() {
    private lateinit var mapView: MapView
    private lateinit var searchBar: EditText

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_map)

        mapView = findViewById(R.id.mapView)
        //KaKao Map UI에 띄우기
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
            }

            override fun onMapError(p0: Exception?) {
            }

        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(p0: KakaoMap) {
            }

        })
        searchBar = findViewById(R.id.search_home)
        searchBar.setOnClickListener {
            val intent = Intent(this, DataSearchActivity::class.java)
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