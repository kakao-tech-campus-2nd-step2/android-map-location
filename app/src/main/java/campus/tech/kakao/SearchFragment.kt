package campus.tech.kakao.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R
import campus.tech.kakao.Model.RetrofitClient
import campus.tech.kakao.Model.SQLiteDb
import campus.tech.kakao.PlaceRepository
import campus.tech.kakao.PlaceViewModel
import campus.tech.kakao.PlaceViewModelFactory

class SearchFragment : Fragment() {
    lateinit var searchView: SearchView
    lateinit var recyclerView: RecyclerView
    lateinit var noResultTextView: TextView
    private lateinit var databaseHelper: SQLiteDb
    lateinit var historyRecyclerView: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var adapter: PlacesAdapter
    private lateinit var placeViewModel: PlaceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews(view)
        setupRecyclerViews()
        setupSearchView()
        setupViewModel()
        updateHistoryData()
    }

    override fun onResume() {
        super.onResume()
        updateHistoryData()
    }

    private fun initializeViews(view: View) {
        searchView = view.findViewById(R.id.searchView2)
        recyclerView = view.findViewById(R.id.recyclerView)
        noResultTextView = view.findViewById(R.id.noResultTextView)
        historyRecyclerView = view.findViewById(R.id.historyRecyclerView)
        databaseHelper = SQLiteDb(requireContext())
    }

    private fun setupRecyclerViews() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        historyRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        historyAdapter = HistoryAdapter(mutableListOf(), { id ->
            databaseHelper.deleteFromSelectedData(id)
            historyAdapter.removeItemById(id)
            updateHistoryData()
        }, { historyItem -> searchView.setQuery(historyItem, true) })
        historyRecyclerView.adapter = historyAdapter

        adapter = PlacesAdapter(listOf()) { name ->
            val id = databaseHelper.insertIntoSelectedData(name)
            updateHistoryData()
        }
        recyclerView.adapter = adapter
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    val apiKey = "KakaoAK ${campus.tech.kakao.map.BuildConfig.KAKAO_REST_API_KEY}"
                    placeViewModel.searchPlaces(apiKey, query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    showNoResultMessage()
                    adapter.updateData(emptyList())
                } else {
                    val apiKey = "KakaoAK ${campus.tech.kakao.map.BuildConfig.KAKAO_REST_API_KEY}"
                    placeViewModel.searchPlaces(apiKey, newText)
                }
                return true
            }
        })
    }

    private fun setupViewModel() {
        val repository = PlaceRepository(RetrofitClient.instance)
        placeViewModel = ViewModelProvider(this, PlaceViewModelFactory(repository)).get(PlaceViewModel::class.java)

        placeViewModel.places.observe(viewLifecycleOwner, Observer { places ->
            if (places?.isEmpty() == true) {
                showNoResultMessage()
            } else {
                hideNoResultMessage()
                places?.let { adapter.updateData(it) }
            }
        })

        placeViewModel.error.observe(viewLifecycleOwner, Observer { error ->
            showNoResultMessage()
        })
    }

    private fun showNoResultMessage() {
        noResultTextView.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        historyRecyclerView.visibility = if (historyAdapter.itemCount > 0) View.VISIBLE else View.GONE
    }

    private fun hideNoResultMessage() {
        noResultTextView.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        historyRecyclerView.visibility = if (historyAdapter.itemCount > 0) View.VISIBLE else View.GONE
    }

    private fun updateHistoryData() {
        val historyData = databaseHelper.getAllSelectedData()
        historyAdapter.updateData(historyData)
        historyRecyclerView.visibility = if (historyData.isNotEmpty()) View.VISIBLE else View.GONE
    }
}