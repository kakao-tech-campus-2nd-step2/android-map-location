package ksc.campus.tech.kakao.map.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraPosition
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import ksc.campus.tech.kakao.map.R
import ksc.campus.tech.kakao.map.models.LocationInfo
import ksc.campus.tech.kakao.map.models.MapViewRepository
import ksc.campus.tech.kakao.map.view_models.SearchActivityViewModel
import java.lang.Exception

class KakaoMapFragment : Fragment() {
    private lateinit var errorTextView: TextView
    private lateinit var retryButton: ImageButton
    private lateinit var errorMessageGroup: Group

    private lateinit var kakaoMapView: MapView
    private var kakaoMap: KakaoMap? = null
    private val viewModel by activityViewModels<SearchActivityViewModel>()

    private fun startKakaoMapView(){
        kakaoMapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
            }

            override fun onMapError(e: Exception?) {
                showErrorText(getErrorMessage(e?.message?:""))
                Log.e("KSC", e?.message ?: "")
            }

        },
            object : KakaoMapReadyCallback() {
                override fun onMapReady(km: KakaoMap) {
                    kakaoMap = km
                    restorePosition()
                    restoreMarker()
                    km.setOnCameraMoveEndListener { _, position, _ ->
                        viewModel.updateCameraPosition(position)
                    }
                }
            })

    }
    private fun initiateKakaoMap(view: View) {
        kakaoMapView = view.findViewById(R.id.kakao_map_view)
        startKakaoMapView()
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

    private fun initiateRetryButton(parent: View){
        retryButton = parent.findViewById(R.id.retry_button)
        retryButton.setOnClickListener{
            startKakaoMapView()
            setErrorMessageVisibility(false)
        }
    }

    private fun initiateViews(parent: View){
        errorTextView = parent.findViewById(R.id.text_error)
        errorMessageGroup = parent.findViewById(R.id.error_message_group)

        initiateRetryButton(parent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_kakao_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initiateViews(view)
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
        kakaoMapView.finish()
        super.onDestroyView()
    }

    private fun restoreMarker(){
        updateSelectedLocation(viewModel.selectedLocation.value)
    }

    private fun restorePosition(){
        kakaoMap?.let {
            if(viewModel.cameraPosition.value != null) {
                moveCamera(
                    it, viewModel.cameraPosition.value!!
                )
            }
        }
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

    private fun getErrorMessage(error: String): String{
        return "지도 인증을 실패 했습니다. \n다시 시도해 주세요.\n\n$error"
    }

    private fun showErrorText(errorMessage:String){
        setErrorMessageVisibility(true)
        errorTextView.text = errorMessage
    }

    private fun setErrorMessageVisibility(visible:Boolean){
        errorMessageGroup.isVisible = visible
    }
}