package campus.tech.kakao.map

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
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

        searchBox.doAfterTextChanged { text ->
            if (text.isNullOrEmpty()) {
                hideResult()
            } else {
                search(text.toString(), false)
            }
        }

        clear.setOnClickListener {
            searchBox.text.clear()
        }

        initSearchResultView()
        initSearchHistoryView()
        observeData()
    }

    override fun deleteHistory(historyName: String) {
        viewModel.deleteHistory(historyName)
    }

    override fun insertHistory(historyName: String) {
        viewModel.insertHistory(historyName)
    }

    private fun hideResult() {
        searchResultView.isVisible = false
        message.isVisible = true
    }
    private fun showResult() {
        searchResultView.isVisible = true
        message.isVisible = false
    }
    private fun search(locName: String, isExactMatch: Boolean) {
        viewModel.searchByKeywordFromServer(locName, isExactMatch)
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
            if (it.isNotEmpty() && (searchBox.text.toString() != "")) {
                showResult()
            } else {
                hideResult()
            }
            searchResultAdapter.refreshList()
        })
    }
}
