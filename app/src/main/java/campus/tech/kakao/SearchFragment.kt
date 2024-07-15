package campus.tech.kakao.View

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R
import campus.tech.kakao.Model.ResultSearchKeyword
import campus.tech.kakao.Model.RetrofitClient
import campus.tech.kakao.Model.SQLiteDb
import campus.tech.kakao.map.BuildConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class SearchFragment : Fragment() {
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var noResultTextView: TextView
    private lateinit var databaseHelper: SQLiteDb
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var adapter: PlacesAdapter

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
        updateHistoryData()
    }

    override fun onResume() {
        super.onResume()
        updateHistoryData()
    }

    private fun initializeViews(view: View) {
        searchView = view.findViewById(R.id.searchView)
        recyclerView = view.findViewById(R.id.recyclerView)
        noResultTextView = view.findViewById(R.id.noResultTextView)
        historyRecyclerView = view.findViewById(R.id.historyRecyclerView)
        databaseHelper = SQLiteDb(requireContext())
    }

    private fun setupRecyclerViews() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        historyRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        historyAdapter = HistoryAdapter(mutableListOf()) { id ->
            databaseHelper.deleteFromSelectedData(id)
            historyAdapter.removeItemById(id)
            updateHistoryData()
        }
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
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    showNoResultMessage()
                    adapter.updateData(emptyList())
                } else {
                    searchPlaces(newText)
                }
                return true
            }
        })
    }

    private fun searchPlaces(query: String) {
        val apiKey = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}"

        RetrofitClient.instance.searchPlaces(apiKey, query).enqueue(object : Callback<ResultSearchKeyword> {
            override fun onResponse(call: Call<ResultSearchKeyword>, response: Response<ResultSearchKeyword>) {
                if (response.isSuccessful && response.body() != null) {
                    val places = response.body()?.documents ?: emptyList()
                    if (places.isEmpty()) {
                        showNoResultMessage()
                    } else {
                        hideNoResultMessage()
                        adapter.updateData(places)
                    }
                } else {
                    showNoResultMessage()
                }
            }

            override fun onFailure(call: Call<ResultSearchKeyword>, t: Throwable) {
                showNoResultMessage()
            }
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
        historyAdapter.notifyDataSetChanged()
        historyRecyclerView.visibility = if (historyData.isNotEmpty()) View.VISIBLE else View.GONE
    }


}