package campus.tech.kakao.map.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.R
import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.generated.callback.OnClickListener
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import java.lang.Exception

class MapActivity : AppCompatActivity() {
    private lateinit var mapView : MapView
    private lateinit var searchView: SearchView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mapView = findViewById(R.id.mapView)

        mapView.start(object : MapLifeCycleCallback(){
            override fun onMapDestroy() {
            }
            override fun onMapError(p0: Exception?) {
            }
        },object :KakaoMapReadyCallback(){
            override fun onMapReady(p0: KakaoMap) {
            }
        })

        searchView = findViewById(R.id.searchView)
        searchView.setOnClickListener{
            val intent = Intent(this, ViewActivity::class.java)
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