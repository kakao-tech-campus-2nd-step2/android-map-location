package campus.tech.kakao.map

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import campus.tech.kakao.map.Data.Place
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class Map_Activity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var searchView: SearchView
    private lateinit var lastKnownLocation: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mapView = findViewById(R.id.map_view)
        searchView = findViewById(R.id.search_text)

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        val preferences = getSharedPreferences("MapPrefs", Context.MODE_PRIVATE)
        val lat = preferences.getFloat("lastLatitude", 0f).toDouble()
        val lng = preferences.getFloat("lastLongitude", 0f).toDouble()
        lastKnownLocation = LatLng(lat, lng)

        setupSearchView()
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val intent = Intent(this, Search_Activity::class.java)
                startActivity(intent)
                searchView.clearFocus()
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        val selectedPlace = intent.getParcelableExtra<Place>("selectedPlace")

        val initialLatLng = if (selectedPlace != null) {
            LatLng(selectedPlace.latitude, selectedPlace.longitude).also {
                addMarkerAndMoveCamera(it, selectedPlace.place_name, selectedPlace.address_name)
            }
        } else {
            lastKnownLocation
        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLatLng, 15f))

        googleMap.setOnMapClickListener { latLng ->
            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(latLng).title("Selected Location"))
            saveLastLocation(latLng.latitude, latLng.longitude)
        }
    }

    private fun addMarkerAndMoveCamera(latLng: LatLng, title: String?, snippet: String?) {
        googleMap.clear()
        googleMap.addMarker(MarkerOptions().position(latLng).title(title).snippet(snippet))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }

    private fun saveLastLocation(latitude: Double, longitude: Double) {
        val preferences = getSharedPreferences("MapPrefs", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putFloat("lastLatitude", latitude.toFloat())
        editor.putFloat("lastLongitude", longitude.toFloat())
        editor.apply()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    fun onMapError(errorCode: Int) {
        if (errorCode == 401) {
            val intent = Intent(this, ErrorActivity::class.java).apply {
                putExtra("error_message", "401 Unauthorized Error")
            }
            startActivity(intent)
        }
    }

}


