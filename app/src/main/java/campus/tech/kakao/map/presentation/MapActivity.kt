package campus.tech.kakao.map.presentation

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import campus.tech.kakao.map.domain.model.Location
import campus.tech.kakao.map.R
import campus.tech.kakao.map.data.repository.HistoryRepositoryImpl
import campus.tech.kakao.map.data.repository.LastLocationRepositoryImpl
import campus.tech.kakao.map.data.repository.ResultRepositoryImpl
import campus.tech.kakao.map.data.source.MapDbHelper
import campus.tech.kakao.map.data.source.RetrofitServiceClient
import campus.tech.kakao.map.domain.repository.HistoryRepository
import campus.tech.kakao.map.domain.repository.ResultRepository
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelLayerOptions
import com.kakao.vectormap.label.LabelManager
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles


class MapActivity : AppCompatActivity() {
    private val TAG = "KAKAOMAP"
    private lateinit var kakaoMapView: MapView
    private lateinit var kakaoMap: KakaoMap
    private lateinit var searchBox: TextView
    private lateinit var infoSheetLayout: LinearLayout
    private lateinit var infoSheetName: TextView
    private lateinit var infoSheetAddress: TextView
    private lateinit var errorLayout: ConstraintLayout
    private lateinit var errorCode: TextView

    private lateinit var viewModel: MapViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        kakaoMapView = findViewById(R.id.kakao_map_view)
        searchBox = findViewById(R.id.search_box)
        infoSheetLayout = findViewById(R.id.info_sheet)
        infoSheetName = findViewById(R.id.info_sheet_name)
        infoSheetAddress = findViewById(R.id.info_sheet_address)
        errorLayout = findViewById(R.id.error_layout)
        errorCode = findViewById(R.id.error_code)

        val dbhelper = MapDbHelper(this)
        val resultRepo = ResultRepositoryImpl(RetrofitServiceClient.retrofitService)
        val historyRepo = HistoryRepositoryImpl(dbhelper)
        val lastRepo = LastLocationRepositoryImpl(dbhelper)
        viewModel = MapViewModel(dbhelper, resultRepo, historyRepo, lastRepo)

        kakaoMapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                // TODO: 필요시 구현 예정
            }

            override fun onMapError(exception: Exception?) {
                hideInfo(exception)
            }

        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                this@MapActivity.kakaoMap = kakaoMap
                val target = viewModel.getLastLocation()
                if (::kakaoMap.isInitialized && target != null) {
                    moveToTargetLocation(kakaoMap, target)
                    showInfo(target)
                }
                Log.d(TAG, "onMapReady")
            }
        })

        searchBox.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        kakaoMapView.resume()
        val target = viewModel.getLastLocation()
        if (::kakaoMap.isInitialized && target != null) {
            moveToTargetLocation(kakaoMap, target)
            showInfo(target)
        }
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        kakaoMapView.pause()
        Log.d("KAKAOMAP", "onPause")
    }

    private fun moveToTargetLocation(kakaoMap: KakaoMap, target: Location) {
        val cameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(target.y, target.x))
        kakaoMap.moveCamera(cameraUpdate)
        val labelManager = kakaoMap.labelManager
        labelManager?.let { setPin(labelManager, target) }
    }

    private fun showInfo(target: Location) {
        infoSheetLayout.isVisible = true
        infoSheetName.text = target.name
        infoSheetAddress.text = target.address
    }

    private fun hideInfo(exception: Exception?) {
        errorCode.text = exception?.message
        errorLayout.isVisible = true
        kakaoMapView.isVisible = false
        searchBox.isVisible = false
    }

    private fun setPin(labelManager: LabelManager, target: Location) {
        labelManager.removeAllLabelLayer()
        val style = labelManager
            .addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.location_label).setTextStyles(30, Color.BLACK)))
        labelManager.layer
            ?.addLabel(LabelOptions.from(LatLng.from(target.y, target.x)).setStyles(style).setTexts(target.name))
    }
}