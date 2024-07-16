package campus.tech.kakao.map.ui.search

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import campus.tech.kakao.map.databinding.ActivitySearchBinding
import campus.tech.kakao.map.model.Place
import campus.tech.kakao.map.model.SavedSearchWord
import campus.tech.kakao.map.ui.ViewModelFactory
import campus.tech.kakao.map.ui.search.adapters.ResultRecyclerViewAdapter
import campus.tech.kakao.map.ui.search.adapters.SavedSearchWordRecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val placeViewModel: PlaceViewModel by viewModels { viewModelFactory }
    private val savedSearchWordViewModel: SavedSearchWordViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        setupObservers()
    }

    /**
     * RecyclerView들을 설정하는 함수.
     */
    private fun setupRecyclerViews() {
        setSearchResultRecyclerView()
        setSavedSearchWordRecyclerView()
    }

    /**
     * view들에 필요한 작업을 처리하는 함수.
     */
    private fun setupViews() {
        setClearImageViewClickListener()
        setSearchEditText()
        setupRecyclerViews()
    }

    /**
     * 검색 EditText가 변경되면 placeViewModel을 통해 검색을 수행하도록 하는 함수.
     */
    private fun setSearchEditText() {
        binding.searchEditText.addTextChangedListener { editable ->
            val categoryInput = editable.toString().trim()
            placeViewModel.searchPlacesByCategory(categoryInput, totalPageCount = 3)
        }
    }

    /**
     * clearImageView의 클릭 리스너를 설정하는 함수.
     *
     * searchEditText의 text를 null로 변경.
     */
    private fun setClearImageViewClickListener() {
        binding.searchClearImageView.setOnClickListener {
            binding.searchEditText.text = null
        }
    }

    interface OnPlaceItemClickListener {
        fun onPlaceItemClicked(place: Place)
    }

    /**
     * 검색 결과를 표시하는 RecyclerView를 설정하는 함수.
     *
     * - `placeItemClickListener` : placeItem을 누르면 검색어가 저장되도록 하는 클릭 리스너 interface 구현 객체
     */
    private fun setSearchResultRecyclerView() {
        val placeItemClickListener =
            object : OnPlaceItemClickListener {
                override fun onPlaceItemClicked(place: Place) {
                    savedSearchWordViewModel.insertSearchWord(
                        SavedSearchWord(
                            name = place.name,
                            placeId = place.id,
                        ),
                    )
                }
            }
        binding.searchResultRecyclerView.adapter = ResultRecyclerViewAdapter(placeItemClickListener)
        binding.searchResultRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    interface OnSavedSearchWordClearImageViewClickListener {
        fun onSavedSearchWordClearImageViewClicked(savedSearchWord: SavedSearchWord)
    }

    /**
     * SavedSearchWordRecyclerView를 설정하는 함수.
     *
     * - `savedSearchWordClearImageViewClickListener` : clear 버튼을 누르면 해당 저장된 검색어가 사라지도록 하는 클릭리스너 interface 구현 객체
     */
    private fun setSavedSearchWordRecyclerView() {
        val savedSearchWordClearImageViewClickListener =
            object : OnSavedSearchWordClearImageViewClickListener {
                override fun onSavedSearchWordClearImageViewClicked(savedSearchWord: SavedSearchWord) {
                    savedSearchWordViewModel.deleteSearchWordById(savedSearchWord)
                }
            }
        binding.savedSearchWordRecyclerView.adapter =
            SavedSearchWordRecyclerViewAdapter(savedSearchWordClearImageViewClickListener)
        binding.savedSearchWordRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    /**
     * viewModel을 관찰하도록 하는 함수.
     */
    private fun setupObservers() {
        collectSearchResults()
        collectSavedSearchWords()
    }

    /**
     * 검색 결과를 관찰하고, RecyclerView에 결과를 반영하는 함수.
     */
    private fun collectSearchResults() {
        lifecycleScope.launch {
            placeViewModel.searchResults.collectLatest { places ->
                (binding.searchResultRecyclerView.adapter as? ResultRecyclerViewAdapter)?.submitList(places)
                binding.noSearchResultTextView.visibility = if (places.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    /**
     * 저장된 검색어를 관찰하고, RecyclerView에 결과를 반영하는 함수.
     */
    private fun collectSavedSearchWords() {
        lifecycleScope.launch {
            savedSearchWordViewModel.savedSearchWords.collectLatest { savedSearchWords ->
                (binding.savedSearchWordRecyclerView.adapter as? SavedSearchWordRecyclerViewAdapter)?.submitList(savedSearchWords)
                binding.savedSearchWordRecyclerView.visibility =
                    if (savedSearchWords.isEmpty()) View.GONE else View.VISIBLE
            }
        }
    }
}
