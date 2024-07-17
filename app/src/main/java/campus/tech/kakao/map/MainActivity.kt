package campus.tech.kakao.map

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import campus.tech.kakao.map.databinding.ActivityMainBinding
import campus.tech.kakao.map.model.Place
import campus.tech.kakao.map.viewModel.MapRepository
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.Label
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import java.lang.Exception


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var kakaoMap: KakaoMap
    private var marker: Bitmap? = null
    private lateinit var label: Label

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = MapRepository(this)

        binding.mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                Log.d("KakaoMap", "카카오맵 종료")
            }

            override fun onMapError(e: Exception?) {
                Log.e("KakaoMap", "카카오맵 인증실패", e)
                setContentView(R.layout.map_error)
                val errorMessage = findViewById<TextView>(R.id.errorMessage)
                errorMessage.text = e?.message
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(p0: KakaoMap) {
                Log.d("KakaoMap", "카카오맵 실행")
                kakaoMap = p0
                marker = vectorToBitmap(R.drawable.pin_drop_24px)
//                addLabel(Place("dd", "xxx", "", "127.115587", "37.406960"))
            }

            override fun getPosition(): LatLng {
                return repository.getLastPos() ?: return super.getPosition()
            }
        })

        Log.d("onCreate", "")

        binding.searchInput.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
            Log.d("kakaomap", "kakaoMap initialized: $kakaoMap")
        }

        binding.searchButton.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }

    private fun addLabel(place: Place) {

        var styles = LabelStyles.from(
            "myLabel",
            LabelStyle.from(marker)
                .setTextStyles(32, Color.BLACK, 1, Color.GRAY)
                .setZoomLevel(kakaoMap.minZoomLevel)
        )

        styles = kakaoMap?.labelManager?.addLabelStyles(styles)

        val latLng = LatLng.from(place.latitude.toDouble(), place.longitude.toDouble())
        moveCamera(latLng)
        label = kakaoMap.labelManager!!.layer!!.addLabel(
            LabelOptions.from(latLng)
                .setStyles(styles).setTexts(place.name)
        )
    }

    private fun moveCamera(latLng: LatLng) {
        val cameraUpdate = CameraUpdateFactory.newCenterPosition(latLng)
        Log.d("kakaomap", "moveCamera: $kakaoMap")
        kakaoMap?.moveCamera(cameraUpdate, CameraAnimation.from(500, true, true))

    }

    private fun vectorToBitmap(drawableId: Int): Bitmap? {
        val drawable = ContextCompat.getDrawable(this, drawableId) ?: return null
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.resume()
        Log.d("Activity State", "Intent is: $intent")
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.pause()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent?.let {
            var place: Place? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                place = intent.getParcelableExtra("place", Place::class.java)
            } else {
                place = intent.getParcelableExtra("place") as Place?
            }
            place?.let {
                addLabel(place)
            }
        }
    }
}
