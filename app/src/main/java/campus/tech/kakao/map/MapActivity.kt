package campus.tech.kakao.map
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.kakao.vectormap.*
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.Label
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.LabelTextBuilder
import kotlin.properties.Delegates

class MapActivity : AppCompatActivity() {
    private lateinit var mapView: MapView
    private lateinit var kakaoMap: KakaoMap
    private lateinit var searchText: EditText
    private lateinit var bottomSheet: ConstraintLayout
    private lateinit var nameTextView: TextView
    private lateinit var addressTextView: TextView
    private var x by Delegates.notNull<Double>()
    private var y by Delegates.notNull<Double>()
    private lateinit var placeName: String
    private var previousLabel: Label? = null
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        placeName = intent.getStringExtra("place_name").toString()
        val addressName = intent.getStringExtra("address")
        x = intent.getStringExtra("x")?.toDoubleOrNull() ?: Double.NaN
        y = intent.getStringExtra("y")?.toDoubleOrNull() ?: Double.NaN

        // UI 요소 초기화
        mapView = findViewById(R.id.map_view)
        searchText = findViewById(R.id.SearchText)
        bottomSheet = findViewById(R.id.bottom_sheet)
        nameTextView = findViewById(R.id.nameTextView)
        addressTextView = findViewById(R.id.addressTextView)

        // SharedPreferences 초기화
        sharedPreferences = getSharedPreferences("map_prefs", Context.MODE_PRIVATE)

        // 이전에 저장된 위치 복원
        val savedX = sharedPreferences.getFloat("last_x", Float.NaN).toDouble()
        val savedY = sharedPreferences.getFloat("last_y", Float.NaN).toDouble()

        // x 값이 null이거나 NaN인 경우 bottomSheet를 숨김
        if (x.isNaN()) {
            bottomSheet.visibility = View.GONE
        } else {
            bottomSheet.visibility = View.VISIBLE

            // mapHeight와 originalMapHeight는 dimens.xml에서 정의된 값을 가져와서 사용
            val mapHeight = resources.getDimensionPixelSize(R.dimen.map_height_expanded)
            val originalMapHeight = resources.getDimensionPixelSize(R.dimen.map_height_original)

            // mapView의 ConstraintLayout.LayoutParams 가져오기
            val params = mapView.layoutParams as ConstraintLayout.LayoutParams

            // mapView의 높이를 조정
            params.height = if (x.isNaN()) originalMapHeight else mapHeight
            mapView.layoutParams = params
        }

        if (x.isNaN() && y.isNaN() ) {
            if (!savedX.isNaN() && !savedY.isNaN()) {
                x = savedX
                y = savedY
            }
        }

        // 장소 이름과 주소를 TextView에 설정
        nameTextView.text = placeName
        addressTextView.text = addressName


        // searchText 클릭 리스너 설정
        searchText.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {}

            override fun onMapError(error: Exception) {
                // 에러 발생 시 ErrorActivity로 전환
                val intent = Intent(this@MapActivity, ErrorActivity::class.java)
                startActivity(intent)
                finish() // 현재 액티비티 종료
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(map: KakaoMap) {
                kakaoMap = map
                if (!x.isNaN() && !y.isNaN()) {
                    if (!placeName.equals("null")) {
                        setMarker(LatLng.from(y, x), placeName)
                    }
                    moveMapCamera(y, x)
                }
            }
        })



        // 주소가 없는 경우 bottomSheet를 숨김
        if (addressName.isNullOrEmpty()) {
            bottomSheet.visibility = View.GONE
        } else {
            bottomSheet.visibility = View.VISIBLE

            // mapHeight와 originalMapHeight는 dimens.xml에서 정의된 값을 가져와서 사용
            val mapHeight = resources.getDimensionPixelSize(R.dimen.map_height_expanded)
            val originalMapHeight = resources.getDimensionPixelSize(R.dimen.map_height_original)

            // mapView의 ConstraintLayout.LayoutParams 가져오기
            val params = mapView.layoutParams as ConstraintLayout.LayoutParams

            // mapView의 높이를 조정
            params.height = if (addressName.isNullOrEmpty()) originalMapHeight else mapHeight
            mapView.layoutParams = params
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putDouble("x", x)
        outState.putDouble("y", y)
        outState.putString("placeName", placeName)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        x = savedInstanceState.getDouble("x", Double.NaN)
        y = savedInstanceState.getDouble("y", Double.NaN)
        placeName = savedInstanceState.getString("placeName", "").toString()

        // UI 상태 복원
        nameTextView.text = placeName
        if (!x.isNaN() && !y.isNaN()) {
            setMarker(LatLng.from(y, x), placeName)
            moveMapCamera(y, x)
        }
    }

    override fun onPause() {
        super.onPause()
        // 액티비티가 일시 정지될 때 현재 위치 저장
        with(sharedPreferences.edit()) {
            putFloat("last_x", x.toFloat())
            putFloat("last_y", y.toFloat())
            apply()
        }
    }

    private fun setMarker(location: LatLng, name: String?) {
        kakaoMap.labelManager?.let { labelManager ->
            // 이전에 추가한 마커가 있다면 삭제
            previousLabel?.let { prevLabel ->
                labelManager.layer?.remove(prevLabel)
            }

            // 마커 스타일 설정
            val markerImageStyle = LabelStyles.from(
                LabelStyle.from(R.drawable.img)
                    .setTextStyles(30, Color.BLACK, 5, Color.WHITE)
            )

            // 마커 추가
            val style = labelManager.addLabelStyles(markerImageStyle)
            val options = LabelOptions.from(location)
                .setStyles(style).setTexts(LabelTextBuilder().setTexts(name))
            previousLabel = labelManager.layer?.addLabel(options)
        }
    }

    private fun moveMapCamera(latitude: Double, longitude: Double) {
        // 카메라 이동
        val cameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(latitude, longitude), 15)
        kakaoMap.moveCamera(cameraUpdate, CameraAnimation.from(500, true, true))
    }
}
