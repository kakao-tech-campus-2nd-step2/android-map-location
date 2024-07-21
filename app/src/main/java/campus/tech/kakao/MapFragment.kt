package campus.tech.kakao.View

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import campus.tech.kakao.map.R
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles

class MapFragment : Fragment() {
    private lateinit var mapView: MapView
    private lateinit var searchView: SearchView
    private var x: Double = 127.108621
    private var y: Double = 37.402005
    private var kakaoMap: KakaoMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val lastLocation = getLastLocation(requireContext())
        y = lastLocation.first
        x = lastLocation.second

        arguments?.let {
            x = it.getDouble("x", x)
            y = it.getDouble("y", y)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = view.findViewById(R.id.KakaoMapView)
        searchView = view.findViewById(R.id.searchView)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })

        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {}

            override fun onMapError(p0: Exception) {
                val intent = Intent(requireContext(), OnMapErrorActivity::class.java)
                intent.putExtra("ErrorType", "${p0}")
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                requireContext().startActivity(intent)
                requireActivity().finish()
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(readyMap: KakaoMap) {
                kakaoMap = readyMap
                val latLng = LatLng.from(y, x)
                Log.d("latlng", "onMapReady: $y $x")

                val cameraUpdate = CameraUpdateFactory.newCenterPosition(latLng)
                readyMap.moveCamera(cameraUpdate)

                readyMap.setOnCameraMoveEndListener { _, _, _ ->
                    val position = readyMap.cameraPosition!!.position
                    saveLastLocation(position.latitude, position.longitude)
                }

                readyMap.setOnMapClickListener { _, _, _, _ ->
                    (activity as? MainActivity)?.showSearchFragment()
                }

                arguments?.let {
                    setCoordinates(it.getDouble("x", x), it.getDouble("y", y), it.getString("placeName", ""), it.getString("roadAddressName", ""))
                }
            }
        })

        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                (activity as? MainActivity)?.showSearchFragment()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        mapView.pause()
    }

    private fun saveLastLocation(lat: Double, lng: Double) {
        val sharedPreferences = requireContext().getSharedPreferences("MapPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putFloat("lastX", lng.toFloat())
            putFloat("lastY", lat.toFloat())
            apply()
        }
    }

    private fun getLastLocation(context: Context): Pair<Double, Double> {
        val sharedPreferences = context.getSharedPreferences("MapPrefs", Context.MODE_PRIVATE)
        val lat = sharedPreferences.getFloat("lastY", 37.402005f).toDouble()
        val lng = sharedPreferences.getFloat("lastX", 127.108621f).toDouble()
        return Pair(lat, lng)
    }

    fun setCoordinates(newX: Double, newY: Double, placeName: String, roadAddressName: String) {
        x = newX
        y = newY
        kakaoMap?.let { map ->
            val latLng = LatLng.from(y, x)
            val cameraUpdate = CameraUpdateFactory.newCenterPosition(latLng)
            map.moveCamera(cameraUpdate)
            setMarker(map, x, y)

            PlaceInfoBottomSheet.newInstance(placeName, roadAddressName)
                .show(parentFragmentManager, PlaceInfoBottomSheet::class.java.simpleName)
        }
    }

    private fun setMarker(kakaoMap: KakaoMap, x: Double, y: Double) {
        kakaoMap.labelManager?.clearAll()
        val styles = kakaoMap.labelManager?.addLabelStyles(
            LabelStyles.from(LabelStyle.from(R.drawable.mapmarker))
        )
        val options = LabelOptions.from(LatLng.from(y, x)).setStyles(styles)
        kakaoMap.labelManager?.layer?.addLabel(options)
    }
}