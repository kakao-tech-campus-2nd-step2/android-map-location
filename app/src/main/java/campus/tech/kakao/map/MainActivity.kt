package campus.tech.kakao.map

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.ActivityMainBinding
import android.util.Log
import com.kakao.sdk.common.util.Utility

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private lateinit var searchAdapter: SearchAdapter
    private lateinit var savedSearchAdapter: SavedSearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate called")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("MainActivity", "View binding initialized")

        setupRecyclerViews()

        viewModel.searchResults.observe(this, Observer { results ->
            Log.d("MainActivity", "Search results updated: $results")
            searchAdapter.updateResults(results)
            binding.searchRecyclerView.visibility = if (results.isEmpty()) View.GONE else View.VISIBLE
            binding.noResult.visibility = if (results.isEmpty())View.VISIBLE else View.GONE
            binding.savedSearchRecyclerView.visibility = if (results.isEmpty()) View.GONE else View.VISIBLE
        })

        viewModel.savedSearches.observe(this, Observer { searches ->
            Log.d("MainActivity", "Saved searches updated: $searches")
            savedSearchAdapter.updateSearches(searches)
        })

        //X 버튼 클릭 시 입력창 초기화
        binding.buttonX.setOnClickListener {
            Log.d("MainActivity", "Clear search input")
            binding.inputSearch.text.clear()
            searchAdapter.updateResults(emptyList())
            binding.searchRecyclerView.visibility = View.GONE
            binding.noResult.visibility = View.VISIBLE
            //binding.savedSearchRecyclerView.visibility = View.VISIBLE
        }

        binding.inputSearch.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus && binding.inputSearch.text.isEmpty()) {
                binding.savedSearchRecyclerView.visibility = View.VISIBLE
            }
        }

        binding.inputSearch.addTextChangedListener ( object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                Log.d("MainActivity", "Search query: $query")
                if(query.isNotEmpty()) {
                    viewModel.searchPlaces(query)
                } else {
                    searchAdapter.updateResults(emptyList())
                    binding.searchRecyclerView.visibility = View.GONE
                    binding.noResult.visibility = View.VISIBLE
                    binding.savedSearchRecyclerView.visibility = View.VISIBLE
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

    }

    fun setupRecyclerViews() {
        searchAdapter = SearchAdapter { result ->
            viewModel.addSearch(result.place_name)
        }

        savedSearchAdapter = SavedSearchAdapter { place ->
            viewModel.removeSearch(place)
        }

        binding.searchRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.searchRecyclerView.adapter = searchAdapter

        binding.savedSearchRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.savedSearchRecyclerView.adapter = savedSearchAdapter

        Log.d("MainActivity", "RecyclerViews set up")
    }

}
