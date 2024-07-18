package campus.tech.kakao.map.view.search

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.model.datasource.LocationLocalDataSource
import campus.tech.kakao.map.model.datasource.LocationRemoteDataSource
import campus.tech.kakao.map.R
import campus.tech.kakao.map.model.SavedLocation
import campus.tech.kakao.map.model.LocationDbHelper
import campus.tech.kakao.map.model.repository.LocationRepository
import campus.tech.kakao.map.model.repository.SavedLocationRepository
import campus.tech.kakao.map.view.map.MapActivity
import campus.tech.kakao.map.viewmodel.ViewModelFactory.LocationViewModelFactory
import campus.tech.kakao.map.viewmodel.ViewModelFactory.SavedLocationViewModelFactory
import campus.tech.kakao.map.viewmodel.LocationViewModel
import campus.tech.kakao.map.viewmodel.SavedLocationViewModel

class MainActivity : AppCompatActivity(), OnItemSelectedListener {
    private val locationViewModel: LocationViewModel by lazy {
        ViewModelProvider(this, LocationViewModelFactory(locationRepository))
        .get(LocationViewModel::class.java)
    }
    private val locationAdapter: LocationAdapter by lazy { LocationAdapter(this) }
    private val locationRecyclerView: RecyclerView by lazy { findViewById(R.id.locationRecyclerView) }

    private val savedLocationViewModel: SavedLocationViewModel by lazy {
        ViewModelProvider(this, SavedLocationViewModelFactory(savedLocationRepository))
        .get(SavedLocationViewModel::class.java)
    }

    private val savedLocationAdapter: SavedLocationAdapter by lazy { SavedLocationAdapter(this) }
    private val savedLocationRecyclerView: RecyclerView by lazy {
        findViewById(R.id.savedLocationRecyclerView)
    }

    private val locationDbHelper: LocationDbHelper by lazy { LocationDbHelper(this) }
    private val locationLocalDataSource: LocationLocalDataSource by lazy { LocationLocalDataSource(locationDbHelper) }
    private val locationRemoteDataSource: LocationRemoteDataSource by lazy { LocationRemoteDataSource() }
    private val locationRepository: LocationRepository by lazy { LocationRepository(locationLocalDataSource, locationRemoteDataSource) }
    private val savedLocationRepository: SavedLocationRepository by lazy { SavedLocationRepository(locationLocalDataSource) }

    private val clearButton: ImageView by lazy { findViewById(R.id.clearButton) }
    private val searchEditText: EditText by lazy { findViewById(R.id.SearchEditTextInMain) }
    private val noResultTextView: TextView by lazy { findViewById(R.id.NoResultTextView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupSearchEditText()
        setupClearButton()
        setupViewModels()
        setupRecyclerViews()
    }

    private fun setupSearchEditText() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                locationViewModel.searchLocationsFromKakaoAPI(query) { searchLocationsSize ->
                    handleNoResultMessage(searchLocationsSize)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        searchEditText.requestFocus()
    }

    private fun setupClearButton() {
        clearButton.setOnClickListener {
            searchEditText.setText("")
        }
    }

    private fun setupViewModels() {
        savedLocationViewModel.setSavedLocation()
        observeSavedLocationViewModel()

        locationViewModel.setLocationsFromKakaoAPI()
        observeLocationsViewModel()
    }

    private fun observeSavedLocationViewModel() {
        savedLocationViewModel.savedLocation.observe(this, Observer {
            savedLocationAdapter.submitList(it?.toList() ?: emptyList())
            if (it.size > 0) {
                savedLocationRecyclerView.visibility = View.VISIBLE
            } else {
                savedLocationRecyclerView.visibility = View.GONE
            }
        })
    }

    private fun observeLocationsViewModel() {
        locationViewModel.searchedLocations.observe(this, Observer {
            locationAdapter.submitList(it?.toList() ?: emptyList())
        })
    }

    private fun setupRecyclerViews() {
        locationRecyclerView.layoutManager = LinearLayoutManager(this)
        locationRecyclerView.adapter = locationAdapter

        savedLocationRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        savedLocationRecyclerView.adapter = savedLocationAdapter
    }

    override fun onLocationViewClicked(title: String, longitude: String, latitude: String, address:String) {
        savedLocationViewModel.addSavedLocation(title)

        val intent = Intent(this@MainActivity, MapActivity::class.java)
        intent.putExtra("title", title)
        intent.putExtra("longitude", longitude)
        intent.putExtra("latitude", latitude)
        intent.putExtra("address", address)
        startActivity(intent)
    }

    override fun onSavedLocationXButtonClicked(item: SavedLocation) {
        savedLocationViewModel.deleteSavedLocation(item)
    }

    override fun onSavedLocationViewClicked(title: String) {
        updateEditText(title)
        locationViewModel.searchLocationsFromKakaoAPI(title){ searchLocationsSize ->
            handleNoResultMessage(searchLocationsSize)
        }
    }

    private fun updateEditText(title: String) {
        searchEditText.setText(title)
        searchEditText.setSelection(searchEditText.text.length)
    }

    private fun handleNoResultMessage(searchLocationsSize: Int) {
        if (searchLocationsSize > 0) {
            noResultTextView.visibility = View.GONE
        } else {
            noResultTextView.visibility = View.VISIBLE
        }
    }
}
