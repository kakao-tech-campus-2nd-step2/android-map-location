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
import androidx.core.view.isVisible
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.R
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView


class MapActivity : AppCompatActivity() {

    lateinit var map: MapView
    lateinit var inputField: EditText
    lateinit var searchIcon: ImageView
    lateinit var errorTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        initVar()
        initSDK()
        initMapView()
        initClickListener()
    }

    private fun initVar() {
        inputField = findViewById<EditText>(R.id.input_search_field)
        searchIcon = findViewById<ImageView>(R.id.search_icon)
        errorTextView = findViewById<TextView>(R.id.error_text)
        bringFrontSearchField()
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

    private fun moveSearchPage(view: View) {
        val intent = Intent(this, SearchActivity::class.java)
        val options = ActivityOptions.makeSceneTransitionAnimation(
            this, view, "inputFieldTransition"
        )
        startActivity(intent, options.toBundle())
    }

    private fun showErrorMessageView(error: String) {
        errorTextView.isVisible = true
        val errorText = getErrorMessage(error) + "\n\n" + error
        errorTextView.text = errorText
    }

    private fun getErrorCode(errorText: String): String {
        val regex = Regex("\\((\\d+)\\)")
        val code = regex.find(errorText)
        Log.d("testt", "errorcode" + code?.groups?.get(1)?.value)
        return code?.groups?.get(1)?.value ?: ""
    }

    private fun getErrorMessage(errorText: String): String {
        val errorCode = getErrorCode(errorText)
        // enum으로 처리?
        when (errorCode) {
            "-1" -> {
                return "인증 과정 중 원인을 알 수 없는 에러가 발생했습니다"
            }
            "-2" -> {
                return "통신 연결 시도 중 에러가 발생하였습니다"
            }
            "-3" -> {
                return "통신 연결 중 SocketTimeoutException 에러가 발생하였습니다"
            }
            "-4" -> {
                return "통신 시도 중 ConnectTimeoutException 에러가 발생하였습니다"
            }
            "400" -> {
                return "요청을 처리하지 못하였습니다"
            }
            "401" -> {
                return "인증 오류가 발생하였습니다. 인증 자격 증명이 충분치 않습니다"
            }
            "403" -> {
                return "권한 오류가 발생하였습니다"
            }
            "429" -> {
                return "정해진 사용량이나, 초당 요청 한도를 초과하였습니다"
            }
            "499" -> {
                return "통신이 실패하였습니다. 인터넷 연결을 확인해주십시오"
            }
            else -> {
                return "오류 코드 X"
            }
        }
    }


}