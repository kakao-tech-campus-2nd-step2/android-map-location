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
import campus.tech.kakao.map.MyApplication.Companion.mapPosition
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdate
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.Label
import com.kakao.vectormap.label.LabelLayer
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import java.lang.Exception

class MapActivity : AppCompatActivity() {
	private lateinit var mapView: MapView
	private var map: KakaoMap? = null
	private lateinit var searchBar: LinearLayout
	private var latitude = 37.402005
	private var longitude = 127.108621
	private lateinit var placeName:String
	private lateinit var addressName:String
	private var styles: LabelStyles? = null
	private lateinit var options:LabelOptions
	private var layer: LabelLayer? = null
	private var label: Label? = null
	private lateinit var bitmapImage: Bitmap
	private lateinit var markerImage: Bitmap
	private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
	private val bottomSheet by lazy { findViewById<LinearLayout>(R.id.bottom_sheet) }
	private val bottomSheetName by lazy { findViewById<TextView>(R.id.name) }
	private val bottomSheetAddress by lazy { findViewById<TextView>(R.id.address) }
	companion object{
		var documentClicked = false
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_map)
		mapView = findViewById(R.id.map_view)
		setMapInfo()
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
				return 17
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
		setMapInfo()
		mapView.resume()
		if(documentClicked){
			makeMarker()
			setBottomSheet()
			documentClicked = false
		}
		else{
			layer?.remove(label)
			bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
		}
		val cameraUpdate: CameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(latitude, longitude))
		map?.moveCamera(cameraUpdate)
	}

	override fun onPause() {
		super.onPause()
		mapView.pause()
	}

	private fun setMapInfo(){
		latitude = mapPosition.getPreferences("latitude","37.406960").toDouble()
		longitude = mapPosition.getPreferences("longitude","127.110030").toDouble()
		placeName = mapPosition.getPreferences("placeName","")
		addressName = mapPosition.getPreferences("addressName","")
	}

	private fun makeMarker(){
		bitmapImage = BitmapFactory.decodeResource(resources, R.drawable.marker)
		markerImage = Bitmap.createScaledBitmap(bitmapImage, 100, 100, true)
		styles = map?.labelManager?.addLabelStyles(LabelStyles.from(LabelStyle.from(markerImage).setTextStyles(40, Color.BLACK)))
		if(styles != null){
			options = LabelOptions.from(LatLng.from(latitude, longitude)).setStyles(styles).setTexts(placeName)
			layer = map?.labelManager?.layer
			if(label != null){
				layer?.remove(label)
			}
			label = layer?.addLabel(options)
		}
		else{
			Log.e("MapActivity", "makeMarker: styles is null")
		}
	}

	private fun initBottomSheet(){
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