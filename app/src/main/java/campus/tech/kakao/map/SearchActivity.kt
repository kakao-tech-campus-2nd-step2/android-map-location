package campus.tech.kakao.map

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import campus.tech.kakao.map.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private val viewModel: SearchViewModel by viewModels { SearchViewModelFactory(applicationContext) }
    private lateinit var searchResultsAdapter: SearchResultsAdapter
    private lateinit var savedKeywordsAdapter: SavedKeywordsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerViews()
        setupAdapters()
        setupObservers()
        setupListeners()
    }

    private fun setupRecyclerViews() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.savedKeywordsRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupAdapters() {
        savedKeywordsAdapter = SavedKeywordsAdapter(emptyList()) { keyword ->
            viewModel.deleteKeyword(keyword)
        }
        binding.savedKeywordsRecyclerView.adapter = savedKeywordsAdapter

        searchResultsAdapter = SearchResultsAdapter(emptyList()) { keyword ->
            viewModel.saveKeyword(keyword)
        }
        binding.recyclerView.adapter = searchResultsAdapter
    }

    private fun setupObservers() {
        viewModel.searchResults.observe(this) { results ->
            searchResultsAdapter.updateData(results)
            binding.noResultsTextView.visibility = if (results.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.savedKeywords.observe(this) { keywords ->
            savedKeywordsAdapter.updateKeywords(keywords)
        }
    }

    private fun setupListeners() {
        binding.clearButton.setOnClickListener {
            binding.editSearch.text.clear()
            searchResultsAdapter.updateData(emptyList())
            binding.noResultsTextView.visibility = View.VISIBLE
        }

        binding.editSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                if (query.isNotEmpty()) {
                    viewModel.search(query)
                    binding.noResultsTextView.visibility = View.GONE
                } else {
                    searchResultsAdapter.updateData(emptyList())
                    binding.noResultsTextView.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}
