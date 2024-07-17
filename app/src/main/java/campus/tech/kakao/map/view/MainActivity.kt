package campus.tech.kakao.map.view

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.viewmodel.DataDbHelper
import campus.tech.kakao.map.viewmodel.LocationAdapter
import campus.tech.kakao.map.Model.LocationData
import campus.tech.kakao.map.Model.RetrofitClient
import campus.tech.kakao.map.Model.Place
import campus.tech.kakao.map.R
import campus.tech.kakao.map.Model.SearchCallback
import campus.tech.kakao.map.Model.SearchResult
import campus.tech.kakao.map.viewmodel.MainViewModel
import campus.tech.kakao.map.viewmodel.SearchViewAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call

class MainActivity : AppCompatActivity() {

    private lateinit var db: DataDbHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var inputText: EditText
    private lateinit var cancelBtn: Button
    private lateinit var resultView: TextView
    private lateinit var searchView: RecyclerView
    private lateinit var locationAdapter: LocationAdapter
    private lateinit var searchAdapter: SearchViewAdapter
    private var locationList = ArrayList<LocationData>()
    private var searchList = ArrayList<LocationData>()

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel.setUiStateChangedListener {uiState ->
            locationList.clear()
            locationList.addAll(uiState.locationList)
            updateRecyclerView()
            resultView.isVisible = uiState.isShowText
        }

        initialize()
        loadSearchList()
        setCancelBtn()
        setSearchView()
        setRecyclerView()
        setSearchListener()

    }

    private fun initialize() {
        recyclerView = findViewById(R.id.recyclerView)
        inputText = findViewById(R.id.inputText)
        cancelBtn = findViewById(R.id.cancelBtn)
        resultView = findViewById(R.id.resultView)
        searchView = findViewById(R.id.searchView)
    }

    private fun setCancelBtn() {
        cancelBtn.setOnClickListener {
            inputText.text.clear()
        }
    }

    private fun setRecyclerView() {
        locationAdapter = LocationAdapter(locationList) { locationData ->
            onItemClick(locationData)
        }
        recyclerView.adapter = locationAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setSearchView() {
        searchAdapter = SearchViewAdapter(searchList) { removedItem ->
            val index = searchList.indexOf(removedItem)
            if (index != -1) {
                searchList.removeAt(index)
                searchAdapter.notifyItemRemoved(index)
                if (searchList.isEmpty()) {
                    searchView.visibility = View.GONE
                }
                saveSearchList()
            }
        }
        searchView.adapter = searchAdapter
        searchView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun saveSearchList() {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val jsonString = Gson().toJson(searchList)
        editor.putString("search_list", jsonString)
        editor.apply()
        Log.d("MainActivity", "Search list saved")
    }

    private fun loadSearchList() {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val jsonString = sharedPref.getString("search_list", null)
        if (jsonString != null) {
            val type = object : TypeToken<ArrayList<LocationData>>() {}.type
            searchList = Gson().fromJson(jsonString, type)
        }
        Log.d("searchList", "$searchList")

        if (searchList.isNotEmpty()) {
            searchView.visibility = View.VISIBLE
            Log.d("MainActivity", "SearchView should be visible, items: ${searchList.size}")
        } else {
            searchView.visibility = View.GONE
            Log.d("MainActivity", "SearchView should be invisible, items: ${searchList.size}")
        }
    }

    private fun setSearchListener() {
        inputText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mainViewModel.searchLocations(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    fun onItemClick(locationData: LocationData) {
        if (!searchList.contains(locationData)) {
            searchList.add(0, locationData)
            searchAdapter.notifyItemInserted(0)
            searchView.scrollToPosition(0)
        } else {
            val index = searchList.indexOf(locationData)
            if (index > 0) {
                searchList.removeAt(index)
                searchList.add(0, locationData)
                searchAdapter.notifyItemMoved(index, 0)
                searchView.scrollToPosition(0)
            }
        }
        searchView.visibility = View.VISIBLE
        saveSearchList()
        Log.d("MainActivity", "Item clicked: ${locationData.name}")
    }

    private fun updateRecyclerView() {
        locationAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        db.close()
        Log.d("MainActivity", "Database closed")
    }
}
