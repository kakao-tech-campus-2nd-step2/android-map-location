package campus.tech.kakao.map.presentation.activity

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.R
import campus.tech.kakao.map.presentation.adapter.SavedSearchAdapter
import campus.tech.kakao.map.presentation.adapter.SearchAdapter
import campus.tech.kakao.map.domain.model.SearchData
import campus.tech.kakao.map.data.SearchDbHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SearchAdapter
    private lateinit var db: SearchDbHelper

    private lateinit var searchWord: EditText
    private lateinit var deleteSearchWord: Button
    private lateinit var searchNothing: TextView

    private lateinit var savedSearchWordRecyclerView: RecyclerView
    private lateinit var savedSearchAdapter: SavedSearchAdapter

    private var searchDataList = mutableListOf<SearchData>()
    private var savedSearchList = mutableListOf<String>()

    private val authorization = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = SearchDbHelper(context = this)

        recyclerView = findViewById(R.id.recyclerView)
        searchWord = findViewById(R.id.searchWord)
        deleteSearchWord = findViewById(R.id.deleteSearchWord)
        searchNothing = findViewById(R.id.searchNothing)
        savedSearchWordRecyclerView = findViewById(R.id.savedSearchWordRecyclerView)

        adapter = SearchAdapter()


        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        savedSearchAdapter = SavedSearchAdapter()

        savedSearchWordRecyclerView.setLayoutManager(
            LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        )

        savedSearchWordRecyclerView.adapter = savedSearchAdapter

        initView()
        saveData()


        searchWord.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchTerm = s.toString()
                if (searchTerm.isEmpty()) {
                    recyclerView.visibility = View.GONE
                    savedSearchWordRecyclerView.visibility = View.GONE
                } else {
                    filterByCategory(searchTerm)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun saveData() {
        lifecycleScope.launch {
            showDb()
            db.fetchApi(authorization)
            loadData()
        }
    }

    private fun loadData() {
        lifecycleScope.launch {
            searchDataList = withContext(Dispatchers.IO) {
                db.loadDb().toMutableList()
            }
            showDb()
        }
    }

    private fun showDb() {
        if (searchWord.text.isEmpty()) {
            adapter.searchDataList = emptyList()
            recyclerView.visibility = View.GONE
            searchNothing.visibility = View.VISIBLE
            savedSearchWordRecyclerView.visibility = View.GONE
        } else {
            adapter.searchDataList = searchDataList
            recyclerView.visibility = View.VISIBLE
            searchNothing.visibility = View.GONE
            savedSearchWordRecyclerView.visibility = View.VISIBLE
        }
        Log.e("Retrofit", "SearchDataList 찾기1: $searchDataList")
        adapter.notifyDataSetChanged()
    }

    private fun deleteWord() {
        deleteSearchWord.setOnClickListener {
            searchWord.text.clear()
            showDb()
        }
    }

    private fun filterByCategory(category: String) {
        val filteredList = searchDataList.filter { it.category == category }
        adapter.searchDataList = filteredList
        adapter.notifyDataSetChanged()

        if (filteredList.isEmpty()) {
            recyclerView.visibility = View.GONE
            searchNothing.visibility = View.VISIBLE
            savedSearchWordRecyclerView.visibility = View.GONE
        } else {
            recyclerView.visibility = View.VISIBLE
            searchNothing.visibility = View.GONE
            savedSearchWordRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun loadSavedWords() {
        savedSearchList = db.getAllSavedWords().toMutableList()
        savedSearchAdapter.savedSearchList = savedSearchList
        savedSearchAdapter.notifyDataSetChanged()
    }

    private fun itemClickSaveWord() {
        adapter.setItemClickListener(object : SearchAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val searchData = adapter.searchDataList[position]
                val wDb = db.writableDatabase
                val values = ContentValues()

                values.put(SearchData.SAVED_SEARCH_COLUMN_NAME, searchData.name)
                wDb.insert(SearchData.SAVED_SEARCH_TABLE_NAME, null, values)
                values.clear()
                savedSearchList = db.getAllSavedWords().toMutableList()
                savedSearchAdapter.savedSearchList = savedSearchList
                savedSearchAdapter.notifyDataSetChanged()

                saveCoordinates(searchData.x, searchData.y)
                saveToBottomSheet(searchData.name, searchData.address)

                val intent = Intent(this@SearchActivity, KakaoMapViewActivity::class.java)
                startActivity(intent)
            }
        })
    }

    private fun saveCoordinates(x: Double, y: Double) {
        val sharedPref = getSharedPreferences("Coordinates", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("xCoordinate", x.toString())
            putString("yCoordinate", y.toString())
            apply()
        }
    }

    private fun saveToBottomSheet(name: String, Address: String) {
        val sharedPref = getSharedPreferences("BottomSheet", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("name", name)
            putString("address", Address)
            apply()
        }
    }

    private fun savedWordClick(){
        savedSearchAdapter.setOnSavedWordClickListener(object :
            SavedSearchAdapter.OnSavedWordClickListener {
            override fun onSavedWordClick(savedWord: String) {
                filterBySavedWord(savedWord)
            }

            override fun onDeleteClick(position: Int) {
                deleteSavedWord(position)
            }
        })
    }

    private fun filterBySavedWord(savedWord: String) {
        val filteredList = searchDataList.filter { it.name == savedWord }
        adapter.searchDataList = filteredList
        adapter.notifyDataSetChanged()

        if (filteredList.isEmpty()) {
            recyclerView.visibility = View.GONE
            searchNothing.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            searchNothing.visibility = View.GONE
        }
    }

    private fun deleteSavedWord(position: Int) {
        val deletedWord = savedSearchAdapter.savedSearchList[position]
        val wDb = db.writableDatabase
        wDb.delete(
            SearchData.SAVED_SEARCH_TABLE_NAME,
            "${SearchData.SAVED_SEARCH_COLUMN_NAME} = ?",
            arrayOf(deletedWord)
        )

        savedSearchAdapter.savedSearchList.removeAt(position)
        savedSearchAdapter.notifyItemRemoved(position)
    }

    private fun initView() {
        itemClickSaveWord()
        deleteWord()
        loadSavedWords()
        savedWordClick()
    }

}



