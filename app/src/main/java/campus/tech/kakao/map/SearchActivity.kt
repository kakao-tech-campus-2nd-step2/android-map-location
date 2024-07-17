package campus.tech.kakao.map

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity(), DatabaseListener {
    private lateinit var viewModel: MapViewModel

    private lateinit var searchBox: EditText
    private lateinit var searchHistoryView: RecyclerView
    private lateinit var searchResultView: RecyclerView
    private lateinit var message: TextView
    private lateinit var clear: ImageButton

    private lateinit var searchResultAdapter: ResultRecyclerAdapter
    private lateinit var searchHistoryAdapter: HistoryRecyclerAdapter
    private val searchBoxWatcher = getSearchBoxWatcher()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val dbHelper = MapDbHelper(this)
        viewModel = MapViewModel(dbHelper)
        searchBox = findViewById(R.id.search_box)
        searchHistoryView = findViewById(R.id.search_history)
        searchResultView = findViewById(R.id.search_result)
        message = findViewById(R.id.message)
        clear = findViewById(R.id.clear)

        searchBox.addTextChangedListener(searchBoxWatcher)

        clear.setOnClickListener {
            searchBox.text.clear()
        }

        initSearchResultView()
        initSearchHistoryView()
        observeData()
    }

    override fun deleteHistory(oldHistory: Location) {
        viewModel.deleteHistory(oldHistory)
    }

    override fun insertHistory(newHistory: Location) {
        viewModel.insertHistory(newHistory)
    }

    override fun showMap(newHistory: Location) {
        val locInfo = Intent(this, MapActivity::class.java)
        locInfo.putExtra(Location.LOCATION, newHistory)
        startActivity(locInfo)
    }

    override fun insertLastLocation(location: Location) {
        viewModel.insertLastLocation(location)
    }

    override fun searchHistory(locName: String, isExactMatch: Boolean) {
        searchBox.removeTextChangedListener(searchBoxWatcher)
        searchBox.setText(locName)
        searchBox.addTextChangedListener(searchBoxWatcher)
        viewModel.searchByKeywordFromServer(locName, isExactMatch)
    }

    private fun search(locName: String, isExactMatch: Boolean) {
        viewModel.searchByKeywordFromServer(locName, isExactMatch)
    }

    private fun hideResult() {
        searchResultView.isVisible = false
        message.isVisible = true
    }
    private fun showResult() {
        searchResultView.isVisible = true
        message.isVisible = false
    }

    private fun initSearchResultView() {
        searchResultAdapter =
            ResultRecyclerAdapter(viewModel.searchResult.value!!, layoutInflater, this)
        searchResultView.adapter = searchResultAdapter
        searchResultView.layoutManager =
            LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
    }

    private fun initSearchHistoryView() {
        searchHistoryAdapter =
            HistoryRecyclerAdapter(viewModel.getAllHistory(), layoutInflater, this)
        searchHistoryView.adapter = searchHistoryAdapter
        searchHistoryView.layoutManager =
            LinearLayoutManager(this@SearchActivity, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun observeData() {
        viewModel.searchHistory.observe(this, Observer {
            searchHistoryAdapter.history = it
            searchHistoryAdapter.refreshList()
        })
        viewModel.searchResult.observe(this, Observer {
            searchResultAdapter.searchResult = it
            if (it.isNotEmpty() && searchBox.text.isNotEmpty()) {
                showResult()
            } else {
                hideResult()
            }
            searchResultAdapter.refreshList()
        })
    }

    private fun getSearchBoxWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // TODO: 필요시 구현 예정
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // TODO: 필요시 구현 예정
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    hideResult()
                } else {
                    search(s.toString(), false)
                }
            }
        }
    }
}
