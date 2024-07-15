package campus.tech.kakao.map

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import java.lang.Exception

class MapActivity : AppCompatActivity() {
	private lateinit var mapView: MapView
	private var map: KakaoMap? = null
	private lateinit var searchBar: LinearLayout
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_map)
		mapView = findViewById(R.id.map_view)
		mapView.start(object : MapLifeCycleCallback() {
			override fun onMapDestroy() {

			}

			override fun onMapError(p0: Exception?) {
				Log.e("MapActivity", "onMapError: ${p0?.message}", p0)
			}

		}, object: KakaoMapReadyCallback() {
			override fun onMapReady(kakaoMap: KakaoMap) {
				map = kakaoMap
			}

		})
		searchBar = findViewById(R.id.search_bar)
		searchBar.setOnClickListener {
			val intent = Intent(this@MapActivity, SearchActivity::class.java)
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