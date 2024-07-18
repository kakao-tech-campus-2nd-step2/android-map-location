package campus.tech.kakao.map.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import campus.tech.kakao.map.repository.SavedSearchKeywordRepository
import campus.tech.kakao.map.model.SearchKeyword
import campus.tech.kakao.map.repository.SearchRepository
import campus.tech.kakao.map.databinding.ActivitySearchWindowBinding
import campus.tech.kakao.map.model.Place
import campus.tech.kakao.map.viewmodel.SearchViewModel
import campus.tech.kakao.map.viewmodel.SearchViewModelFactory
import kotlinx.coroutines.launch

class SearchWindowActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchWindowBinding
    private lateinit var viewModel: SearchViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchWindowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val searchRepository = SearchRepository()
        val savedSearchKeywordRepository = SavedSearchKeywordRepository(this)
        val viewModelProviderFactory =
            SearchViewModelFactory(searchRepository, savedSearchKeywordRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[SearchViewModel::class.java]

        delSearchKeywordListener()
        detectSearchWindowChangedListener()
        displaySearchResults()
        displaySavedSearchKeywords()
    }

    private fun delSearchKeywordListener() {
        binding.delSearchKeyword.setOnClickListener {
            binding.searchWindow.text = null
        }
    }

    private fun detectSearchWindowChangedListener() {
        binding.searchWindow.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchKeyWord = SearchKeyword(s.toString())
                viewModel.getSearchResults(searchKeyWord)
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    private fun displaySearchResults() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchResults.collect {
                    if (it.isEmpty()) {
                        showView(binding.emptySearchResults, true)
                        showView(binding.searchResultsList, false)
                    } else {
                        showView(binding.emptySearchResults, false)
                        showView(binding.searchResultsList, true)

                        val adapter = SearchResultsAdapter(it, layoutInflater)
                        adapter.setItemClickListener(object :
                            SearchResultsAdapter.OnItemClickListener {
                            override fun onClick(item: Place) {
                                val searchKeyword = SearchKeyword(item.place_name)
                                viewModel.saveSearchKeyword(searchKeyword)

                                val intent = Intent()
                                intent.putExtra(ActivityKeys.INTENT_PLACE, item)
                                setResult(RESULT_OK, intent)
                                finish()
                            }
                        })

                        binding.searchResultsList.adapter = adapter
                        binding.searchResultsList.layoutManager =
                            LinearLayoutManager(this@SearchWindowActivity)
                    }
                }
            }
        }
    }

    private fun displaySavedSearchKeywords() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.savedSearchKeywords.collect {

                    val adapter = SavedSearchKeywordsAdapter(it, layoutInflater)
                    adapter.setItemClickListener(object :
                        SavedSearchKeywordsAdapter.OnItemClickListener {
                        override fun onClickSavedSearchKeyword(item: SearchKeyword) {
                            binding.searchWindow.setText(item.searchKeyword)
                        }

                        override fun onClickDelSavedSearchKeyword(item: SearchKeyword) {
                            viewModel.delSavedSearchKeyword(item)
                        }
                    })

                    binding.savedSearchKeywordsList.adapter = adapter
                    binding.savedSearchKeywordsList.layoutManager = LinearLayoutManager(
                        this@SearchWindowActivity, LinearLayoutManager.HORIZONTAL, false
                    )
                }
            }
        }
    }

    private fun showView(view: View, isShow: Boolean) {
        view.visibility = if (isShow) View.VISIBLE else View.GONE
    }
}
