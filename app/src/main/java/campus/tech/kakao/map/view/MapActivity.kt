package campus.tech.kakao.map.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityMapBinding
import campus.tech.kakao.map.model.Location
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapAuthException
import com.kakao.vectormap.MapLifeCycleCallback

class MapActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMapBinding
    private lateinit var activityLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        activityLauncher = setActivityLauncher()

        binding.searchBackgroundView.setOnClickListener {
            val intent = Intent(this@MapActivity, SearchLocationActivity::class.java)
            activityLauncher.launch(intent)
        }
    }

    private fun setActivityLauncher(): ActivityResultLauncher<Intent> {
        return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val markerLocation = if (Build.VERSION.SDK_INT >= 33) {
                    it.data?.getSerializableExtra("markerLocation", Location::class.java)
                } else {
                    it.data?.getSerializableExtra("markerLocation") as Location?
                }

                markerLocation?.let {
                    // TODO: 마커 찍기

                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        binding.kakaoMapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                Log.d("MapActivity", "onMapDestroy")
            }

            override fun onMapError(e: Exception?) {
                val intent = Intent(this@MapActivity, MapErrorActivity::class.java)

                val errorDescription = getString(
                    when ((e as MapAuthException).errorCode) {
                        401 -> R.string.Error401
                        403 -> R.string.Error403
                        429 -> R.string.Error429
                        499 -> R.string.Error499
                        else -> R.string.ErrorDefault
                    }
                )
                intent.putExtra("errorDescription", errorDescription)
                intent.putExtra("errorCode", e.message)

                startActivity(intent)
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(map: KakaoMap) {
                Log.d("MapActivity", "onMapReady")
            }
        })
    }
}