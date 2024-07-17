package campus.tech.kakao.map.view

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.R
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView


class MapActivity : AppCompatActivity() {

    lateinit var map : MapView
    lateinit var inputField : EditText
    lateinit var searchIcon : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        initVar()
        initSDK()
        initMapView()
        initClickListener()
    }

    private fun initVar(){
        inputField = findViewById<EditText>(R.id.input_search_field)
        searchIcon = findViewById<ImageView>(R.id.search_icon)
        bringFrontSearchField()
    }

    private fun initSDK(){
        KakaoMapSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }

    private fun initMapView(){
        map = findViewById<MapView>(R.id.map_view)
        map.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                Log.d("testt", "MapDestroy")
            }

            override fun onMapError(error: Exception) {
                Log.d("testt", error.toString())
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                Log.d("testt", "MapReady")
            }
        })

    }

    private fun bringFrontSearchField(){
        inputField.bringToFront()
        searchIcon.bringToFront()
    }

    private fun initClickListener(){
        inputField.setOnClickListener{
            moveSearchPage(it)
        }
    }

    private fun moveSearchPage(view : View){
        val intent = Intent(this, SearchActivity::class.java)
        val options = ActivityOptions.makeSceneTransitionAnimation(
            this, view, "inputFieldTransition")
        startActivity(intent, options.toBundle())
    }
}