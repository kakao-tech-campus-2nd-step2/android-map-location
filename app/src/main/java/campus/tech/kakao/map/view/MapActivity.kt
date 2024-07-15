package campus.tech.kakao.map.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import campus.tech.kakao.map.databinding.ActivityMapBinding
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapAuthException
import com.kakao.vectormap.MapLifeCycleCallback

class MapActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.kakaoMapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                Log.d("MapActivity", "onMapDestroy")
            }

            override fun onMapError(e: Exception?) {
                Log.e("MapActivity", "onMapError", e)
                Toast.makeText(
                    this@MapActivity,
                    when ((e as MapAuthException).errorCode) {
                        401 -> "API 인증에 실패했습니다.\n올바른 API 키를 설정해주세요."
                        499 -> "서버와의 통신에 실패했습니다.\n인터넷 연결을 확인해주세요."
                        else -> "오류가 발생했습니다. 다시 시도해주세요."
                    },
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(map: KakaoMap) {
                Log.d("MapActivity", "onMapReady")
            }
        })

        binding.searchBackgroundView.setOnClickListener {
            val intent = Intent(this@MapActivity, SearchLocationActivity::class.java)
            startActivity(intent)
        }
    }
}