package campus.tech.kakao.map

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdate
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import java.lang.Exception

const val MARKER_WIDTH = 100
const val MARKER_HEIGHT = 100
const val MARKER_TEXT_SIZE = 40
const val ZOOM_LEVEL = 17
class MapActivity : AppCompatActivity() {
	private lateinit var mapView: MapView
	private var map: KakaoMap? = null
	private lateinit var searchBar: LinearLayout
	private lateinit var model: MainViewModel
	private lateinit var placeName:String
	private lateinit var addressName:String
	private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
	private lateinit var bottomSheet :LinearLayout
	private lateinit var bottomSheetName:TextView
	private lateinit var bottomSheetAddress :TextView
	private var longitude:Double = 0.0
	private var latitude:Double = 0.0
	companion object{
		var documentClicked = false
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_map)
		mapView = findViewById(R.id.map_view)
		model = ViewModelProvider(this)[MainViewModel::class.java]
		getMapInfo()
		mapView.start(object : MapLifeCycleCallback() {
			override fun onMapDestroy() {

			}

			override fun onMapError(p0: Exception?) {
				setContentView(R.layout.map_error)
				val errorText = findViewById<TextView>(R.id.map_error_text)
				errorText.text = p0?.message
			}

		}, object: KakaoMapReadyCallback() {
			override fun onMapReady(kakaoMap: KakaoMap) {
				map = kakaoMap
			}

			override fun getPosition(): LatLng {
				return LatLng.from(latitude, longitude)
			}

			override fun getZoomLevel(): Int {
				return ZOOM_LEVEL
			}
		})
		searchBar = findViewById(R.id.search_bar)
		searchBar.setOnClickListener {
			val intent = Intent(this@MapActivity, SearchActivity::class.java)
			startActivity(intent)
		}
		initBottomSheet()
	}

	override fun onResume() {
		super.onResume()
		getMapInfo()
		mapView.resume()
		if(documentClicked){
			makeMarker()
			setBottomSheet()
			documentClicked = false
		}
		else{
			map?.labelManager?.layer?.removeAll()
			bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
		}
		val cameraUpdate: CameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(latitude, longitude))
		map?.moveCamera(cameraUpdate)
	}

	override fun onPause() {
		super.onPause()
		mapView.pause()
	}

	private fun getMapInfo(){
		val mapInfoList = model.getMapInfo()
		latitude = mapInfoList[0].toDouble()
		longitude = mapInfoList[1].toDouble()
		placeName = mapInfoList[2]
		addressName = mapInfoList[3]
	}

	private fun makeMarker(){
		val bitmapImage = BitmapFactory.decodeResource(resources, R.drawable.marker)
		val markerImage = Bitmap.createScaledBitmap(bitmapImage, MARKER_WIDTH, MARKER_HEIGHT, true)
		val styles = map?.labelManager?.addLabelStyles(LabelStyles.from(LabelStyle.from(markerImage).setTextStyles(
			MARKER_TEXT_SIZE, Color.BLACK)))
		if(styles != null){
			val options = LabelOptions.from(LatLng.from(latitude, longitude)).setStyles(styles).setTexts(placeName)
			map?.labelManager?.layer?.removeAll()
			map?.labelManager?.layer?.addLabel(options)
		}
		else{
			Log.e("MapActivity", "makeMarker: styles is null")
		}
	}

	private fun initBottomSheet(){
		bottomSheet = findViewById(R.id.bottom_sheet)
		bottomSheetName = findViewById(R.id.name)
		bottomSheetAddress = findViewById(R.id.address)
		bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
		bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
			override fun onStateChanged(bottomSheet: View, newState: Int) {
			}

			override fun onSlide(bottomSheet: View, slideOffset: Float) {
			}

		})
		bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
	}

	private fun setBottomSheet(){
		bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
		bottomSheetName.text = placeName
		bottomSheetAddress.text = addressName
	}
}