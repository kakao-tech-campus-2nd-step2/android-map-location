package campus.tech.kakao.map.view.map

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityMapBinding
import campus.tech.kakao.map.model.state.UiState
import campus.tech.kakao.map.view.ErrorView
import campus.tech.kakao.map.view.search.SearchActivity
import campus.tech.kakao.map.viewmodel.map.MapViewModel
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapLifeCycleCallback
import java.lang.Exception

class MapActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMapBinding
    private lateinit var mapViewModel: MapViewModel
    private var errorView: ErrorView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        mapViewModel = MapViewModel()
        checkLocationPermission()
        setupMapView()
        setupSearchClickListener()
        observeUiState()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.pause()
    }

    private fun setupBinding() {
        binding = ActivityMapBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun checkLocationPermission(): Boolean {
        val locationPermissions =
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        kotlin.runCatching {
            if (
                ActivityCompat.checkSelfPermission(
                    this,
                    locationPermissions[0],
                ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    locationPermissions[1],
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    locationPermissions,
                    100,
                )
                return false
            } else {
                return true
            }
        }.onFailure {
            Log.e("MapActivity", "locationError : ${it.message}}")
        }
        return false
    }

    private fun setupMapView() {
        binding.mapView.start(
            object : MapLifeCycleCallback() {
                override fun onMapDestroy() {
                    // 지도 API 가 정상적으로 종료될 때 호출됨
                }

                override fun onMapError(error: Exception?) {
                    // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출됨
                    error?.let {
                        mapViewModel.showErrorView(it)
                    }
                }
            },
            object : KakaoMapReadyCallback() {
                override fun onMapReady(kakaoMap: KakaoMap) {
                    // 인증 후 API 가 정상적으로 실행될 때 호출됨
                    mapViewModel.showSuccessView()
                }
            },
        )
    }

    private fun setupSearchClickListener() {
        binding.flowSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeUiState() {
        mapViewModel.uiState.observe(this) {
            when (it) {
                is UiState.Error -> showMapAuthErrorView(it.e)

                UiState.Success -> hideErrorView()

                UiState.Loading -> {
                    /** do nothing **/
                }

                UiState.NotInitialized -> {
                    /** do nothing **/
                }
            }
        }
    }

    private fun showMapAuthErrorView(e: Exception) {
        errorView =
            ErrorView(
                context = this,
                errorMessage = getString(R.string.map_auth_error).plus("\n\n${e.message}"),
                retry = { setupMapView() },
            )
        if (errorView?.isAttachedToWindow == false) {
            (binding.root as? ViewGroup)?.apply {
                removeView(errorView)
                addView(
                    errorView,
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    ),
                )
            }
        }
    }

    private fun hideErrorView() {
        if (errorView?.isAttachedToWindow == true) {
            (binding.root as? ViewGroup)?.apply {
                removeView(errorView)
            }
        }
    }
}
