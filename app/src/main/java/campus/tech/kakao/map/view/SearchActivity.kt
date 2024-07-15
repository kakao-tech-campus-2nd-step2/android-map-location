package campus.tech.kakao.map.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import campus.tech.kakao.map.databinding.ActivitySearchBinding
import campus.tech.kakao.map.viewmodel.PlaceAdapter
import campus.tech.kakao.map.viewmodel.PlaceViewModel
import campus.tech.kakao.map.viewmodel.PlaceViewModelFactory
import campus.tech.kakao.map.viewmodel.SavedPlaceAdapter

class SearchActivity : AppCompatActivity() {

    private val viewModel: PlaceViewModel by viewModels { PlaceViewModelFactory(this) }
    private lateinit var binding: ActivitySearchBinding
    private lateinit var placeAdapter: PlaceAdapter
    private lateinit var savedPlaceAdapter: SavedPlaceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerViews()
        observeViewModel()
        setupSearchEditText()
    }

    private fun initRecyclerViews() {
        binding.recyclerViewSearchResults.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewSavedSearches.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        placeAdapter = PlaceAdapter(emptyList()) { place ->
            viewModel.addSavedQuery(place.placeName)
        }
        binding.recyclerViewSearchResults.adapter = placeAdapter

        savedPlaceAdapter = SavedPlaceAdapter(emptyList()) { query ->
            viewModel.removeSavedQuery(query)
        }
        binding.recyclerViewSavedSearches.adapter = savedPlaceAdapter
    }

    private fun observeViewModel() {
        viewModel.places.observe(this, Observer { places ->
            placeAdapter.updateItems(places)
        })

        viewModel.savedQueries.observe(this, Observer { queries ->
            savedPlaceAdapter.updateItems(queries)
        })
    }

    private fun setupSearchEditText() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrBlank()) {
                    viewModel.loadPlaces(s.toString(), "")
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}
