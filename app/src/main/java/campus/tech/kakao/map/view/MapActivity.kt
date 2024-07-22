package campus.tech.kakao.map.view

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.isVisible
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.R
import campus.tech.kakao.map.model.Constants
import campus.tech.kakao.map.model.Place
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelManager
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles

class MapActivity : AppCompatActivity() {

    lateinit var map: MapView
    lateinit var inputField: EditText
    lateinit var searchIcon: ImageView
    lateinit var errorTextView: TextView
    lateinit var kakaoMap : KakaoMap
    lateinit var resultLauncher : ActivityResultLauncher<Intent>
    lateinit var sharedPreferences : SharedPreferences
    lateinit var bottomSheetLayout : ConstraintLayout
    lateinit var placeNameField : TextView
    lateinit var placeLocationField : TextView
    lateinit var bottomSheetBehavior : BottomSheetBehavior<ConstraintLayout>
    lateinit var editor : Editor

    companion object ChonnamUnivLocation {
        const val LATITUDE = "35.175487"
        const val LONGITUDE = "126.907163"
    }

    enum class ErrorCode(val code: String, val errorMessage : String){
        UNKNOWN_ERROR("-1", "인증 과정 중 원인을 알 수 없는 에러가 발생했습니다"),
        CONNECTION_ERROR("-2", "통신 연결 시도 중 에러가 발생하였습니다"),
        SOCKET_TIMEOUT("-3", "통신 연결 중 SocketTimeoutException 에러가 발생하였습니다"),
        CONNECT_TIMEOUT("-4", "통신 시도 중 ConnectTimeoutException 에러가 발생하였습니다"),
        BAD_REQUEST("400", "요청을 처리하지 못하였습니다"),
        AUTHORIZED_FAILURE("401", "인증 오류가 발생하였습니다. 인증 자격 증명이 충분치 않습니다"),
        FORBIDDEN("403", "권한 오류가 발생하였습니다"),
        TOO_MANY_REQUESTS("429", "정해진 사용량이나, 초당 요청 한도를 초과하였습니다"),
        CONNECTION_FAILURE("499", "통신이 실패하였습니다. 인터넷 연결을 확인해주십시오"),
        UNKNOWN("X", "오류 코드 X");

        companion object {
            fun getErrorMessage(errorText: String): ErrorCode {
                return entries.find { errorText == it.code } ?: UNKNOWN
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        initVar()
        initSharedPreferece()
        initBottomSheet()
        initSDK()
        initMapView()
        initClickListener()
        initResultLauncher()
    }



    private fun initVar() {
        inputField = findViewById<EditText>(R.id.input_search_field)
        searchIcon = findViewById<ImageView>(R.id.search_icon)
        errorTextView = findViewById<TextView>(R.id.error_text)
        placeNameField = findViewById<TextView>(R.id.place_name)
        placeLocationField = findViewById<TextView>(R.id.place_location)
        bringFrontSearchField()
    }

    private fun initSharedPreferece(){
        sharedPreferences = getSharedPreferences(Constants.Keys.KEY_SHARED, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    private fun initBottomSheet(){
        bottomSheetLayout = findViewById<ConstraintLayout>(R.id.bottom_sheet)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun initSDK() {
        KakaoMapSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }

    private fun initMapView() {
        map = findViewById<MapView>(R.id.map_view)
        map.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                Log.d("testt", "MapDestroy")
            }

            override fun onMapError(error: Exception) {
                Log.d("testt", "string : " + error.toString())
                Log.d("testt", "message : " + error.message.toString())
                Log.d("testt", "hashCode : " + error.localizedMessage)
                showErrorMessageView(error.message.toString())
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                Log.d("testt", "MapReady")
                errorTextView.isVisible = false
                this@MapActivity.kakaoMap = kakaoMap
            }

            override fun getPosition(): LatLng {
                val latitude = sharedPreferences.getString(Constants.Keys.KEY_LATITUDE, LATITUDE)?.toDouble() ?: LATITUDE.toDouble()
                val longitude = sharedPreferences.getString(Constants.Keys.KEY_LONGITUDE, LONGITUDE)?.toDouble() ?: LONGITUDE.toDouble()
                return LatLng.from(latitude, longitude)
            }
        })
    }

    private fun bringFrontSearchField() {
        inputField.bringToFront()
        searchIcon.bringToFront()
    }

    private fun initClickListener() {
        inputField.setOnClickListener {
            moveSearchPage(it)
        }
    }

    private fun initResultLauncher(){
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val place = getPlaceToResult(result)
                    val latitude = place?.y?.toDouble() ?: LATITUDE.toDouble()
                    val longitude = place?.x?.toDouble()?: LONGITUDE.toDouble()
                    val pos = LatLng.from(latitude, longitude)
                    moveMapCamera(pos)
                    createLabel(pos)
                    putPosSharedPreference(latitude, longitude)
                    bottomSheetBinding(place)
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
    }

    private fun moveSearchPage(view: View) {
        val intent = Intent(this, SearchActivity::class.java)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this, view, "inputFieldTransition"
        )
        resultLauncher.launch(intent, options)
    }

    private fun showErrorMessageView(error: String) {
        errorTextView.isVisible = true
        val errorCode = getErrorCode(error)
        val errorText = ErrorCode.getErrorMessage(errorCode).errorMessage + "\n\n" + error
        errorTextView.text = errorText
    }

    private fun getErrorCode(errorText: String): String {
        val regex = Regex("\\((\\d+)\\)")
        val code = regex.find(errorText)
        Log.d("testt", "errorcode" + code?.groups?.get(1)?.value)
        return code?.groups?.get(1)?.value ?: ""
    }

    private fun getPlaceToResult(result: ActivityResult): Place? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            result.data?.getParcelableExtra(Constants.Keys.KEY_PLACE, Place::class.java)
        } else {
            result.data?.getParcelableExtra(Constants.Keys.KEY_PLACE)
        }
    }

    private fun moveMapCamera(pos : LatLng){
        kakaoMap.moveCamera(CameraUpdateFactory.newCenterPosition(pos))
    }

    private fun bottomSheetBinding(place: Place?){
        placeNameField.text = place?.name ?: ""
        placeLocationField.text = place?.location ?: ""
    }

    private fun putPosSharedPreference(latitude : Double, longitude : Double){
        editor.putString(Constants.Keys.KEY_LATITUDE, latitude.toString())
        editor.putString(Constants.Keys.KEY_LONGITUDE, longitude.toString())
        editor.apply()
    }

    private fun removeAllLabel(){
        kakaoMap.labelManager?.clearAll()
    }

    private fun createLabel(pos : LatLng){
        val labelManager = kakaoMap.labelManager
        removeAllLabel()
        val style = labelManager
            ?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.ic_location_marker_2).setAnchorPoint(0.5f, 1f)))
        var label = kakaoMap.getLabelManager()?.getLayer()?.addLabel(LabelOptions.from("center",pos).setStyles(style).setRank(1))
    }
}