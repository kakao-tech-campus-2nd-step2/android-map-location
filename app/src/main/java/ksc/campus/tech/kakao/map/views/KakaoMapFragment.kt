package ksc.campus.tech.kakao.map.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraPosition
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.Label
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import ksc.campus.tech.kakao.map.R
import ksc.campus.tech.kakao.map.models.LocationInfo
import ksc.campus.tech.kakao.map.models.MapViewRepository
import ksc.campus.tech.kakao.map.view_models.SearchActivityViewModel
import java.lang.Exception

class KakaoMapFragment : Fragment() {
    private lateinit var kakaoMapView: MapView
    private var kakaoMap: KakaoMap? = null
    private val viewModel by activityViewModels<SearchActivityViewModel>()

    private fun initiateKakaoMap(view: View) {
        kakaoMapView = view.findViewById(R.id.kakao_map_view)
        kakaoMapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
            }

            override fun onMapError(e: Exception?) {
                Log.e("KSC", e?.message ?: "")
            }

        },
            object : KakaoMapReadyCallback() {
                override fun onMapReady(km: KakaoMap) {
                    kakaoMap = km
                    restorePosition(km)
                    restoreMarker()
                    km.setOnCameraMoveEndListener { _, position, _ ->
                        viewModel.updateCameraPosition(position)
                    }
                }
            })
    }

    private fun moveCamera(kakaoMap: KakaoMap, position: CameraPosition){
        val camUpdate =
            CameraUpdateFactory.newCameraPosition(position)
        kakaoMap.moveCamera(camUpdate)
    }

    private fun initiateViewModelCallbacks(){
        viewModel.selectedLocation.observe(viewLifecycleOwner){
            updateSelectedLocation(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_kakao_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initiateKakaoMap(view)
        initiateViewModelCallbacks()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        kakaoMapView.resume()
        super.onResume()
    }

    override fun onPause() {
        kakaoMapView.pause()
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun restoreMarker(){
        updateSelectedLocation(viewModel.selectedLocation.value)
    }

    private fun restorePosition(kakaoMap: KakaoMap){
        moveCamera(kakaoMap, viewModel.cameraPosition.value?:MapViewRepository.initialCameraPosition)
    }

    private fun updateSelectedLocation(locationInfo: LocationInfo?){
        if (locationInfo == null) {
            clearLabels()
        } else {
            changedSelectedPosition(LatLng.from(locationInfo.latitude, locationInfo.longitude))
        }
    }

    private fun changedSelectedPosition(coordinate: LatLng){
        clearLabels()
        addLabel(coordinate)
    }

    private fun clearLabels(){
        if(kakaoMap?.isVisible != true) {
            Log.d("KSC", "mapView not activated")
            return
        }
        val layer = kakaoMap?.labelManager?.layer
        layer?.removeAll()
    }

    private fun addLabel(coordinate:LatLng){
        if(kakaoMap?.isVisible != true) {
            Log.d("KSC", "mapView not activated")
            return
        }
        val styles = kakaoMap?.labelManager
            ?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.map_pin)))
        val options = LabelOptions.from(coordinate)
        options.setStyles(styles)
        val layer = kakaoMap?.labelManager?.layer
        layer?.addLabel(options)
    }
}