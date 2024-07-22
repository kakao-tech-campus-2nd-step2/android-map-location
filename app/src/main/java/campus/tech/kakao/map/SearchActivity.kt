package campus.tech.kakao.map

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.dto.Place
import campus.tech.kakao.map.repository.KakaoRepository

class SearchActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var resultRecyclerView: RecyclerView
    private lateinit var searchHistoryRecyclerView: RecyclerView
    private lateinit var noResults: TextView
    private lateinit var resultRecyclerViewAdapter: ResultRecyclerViewAdapter
    private lateinit var searchHistoryRecyclerViewAdapter: SearchHistoryRecyclerViewAdapter
    private lateinit var placeList: List<Place>
    private lateinit var searchHistoryList: MutableList<Place>
    private lateinit var kakaoRepository: KakaoRepository
    private lateinit var backButton: ImageButton
    private lateinit var mapX: String
    private lateinit var mapY: String
    private lateinit var name: String
    private lateinit var address: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("here", "I'm in SearchActivity")
        setContentView(R.layout.activity_search)

        searchEditText = findViewById(R.id.search_edit_text)
        resultRecyclerView = findViewById(R.id.recycler_view)
        searchHistoryRecyclerView = findViewById(R.id.horizontal_recycler_view)
        noResults = findViewById(R.id.no_results)
        backButton = findViewById(R.id.back_button)

        placeList = emptyList()
        kakaoRepository = (application as KyleMaps).kakaoRepository

        val searchHistoryDB = SearchHistoryDBHelper(this)
        searchHistoryList = searchHistoryDB.getAllSearchHistory()

        resultRecyclerViewAdapter = ResultRecyclerViewAdapter(
            places = emptyList(),
            onItemClick = { place ->
                searchHistoryDB.insertSearchHistory(place)
                updateSearchHistoryRecyclerView(place)
                updateMapPosition(place)
                goBackToMap()
            }
        )
        resultRecyclerView.adapter = resultRecyclerViewAdapter
        resultRecyclerView.layoutManager = LinearLayoutManager(this)

        searchHistoryRecyclerViewAdapter = SearchHistoryRecyclerViewAdapter(
            searchHistory = searchHistoryList,
            onItemClick = { index ->
                searchEditText.setText(searchHistoryList[index].place_name)
                searchEditText.clearFocus()
                searchEditText.isFocusable = false
            },
            onItemDelete = { index ->
                if (index >= 0 && index < searchHistoryList.size) {
                    val deletedItemName = searchHistoryList[index].place_name
                    searchHistoryList.removeAt(index)
                    searchHistoryDB.deleteSearchHistoryByName(deletedItemName)
                    searchHistoryRecyclerViewAdapter.notifyItemRemoved(index)
                }
            }
        )
        searchHistoryRecyclerView.adapter = searchHistoryRecyclerViewAdapter
        searchHistoryRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        searchEditText.addTextChangedListener { text ->
            text?.let { searchPlaces(it.toString()) }
        }

        backButton.setOnClickListener {
            goBackToMap()
        }
    }

    fun searchPlaces(query: String) {
        kakaoRepository.searchPlaces(query) { places ->
            runOnUiThread {
                placeList = places
                resultRecyclerViewAdapter.setPlaces(places)
                showNoResultsMessage(places.isEmpty())
                Log.d("searchPlaces", "searchPlaces: ${resultRecyclerViewAdapter.itemCount}")
            }
        }
    }

    private fun showNoResultsMessage(show: Boolean) {
        if (show) {
            noResults.visibility = TextView.VISIBLE
            resultRecyclerView.visibility = RecyclerView.GONE
            Log.d("visibility", "noresult: ${noResults.visibility}, recycler: ${resultRecyclerView.visibility} ")
        } else {
            noResults.visibility = TextView.GONE
            resultRecyclerView.visibility = RecyclerView.VISIBLE
            Log.d("visibility", "noresult: ${noResults.visibility}, recycler: ${resultRecyclerView.visibility} ")

        }
    }

    private fun updateSearchHistoryRecyclerView(place: Place) {
        searchHistoryList.add(place)
        searchHistoryRecyclerViewAdapter.notifyDataSetChanged()
    }

    private fun goBackToMap() {
        val searchToMapIntent = Intent(this, MapActivity::class.java)
        searchToMapIntent.putExtra("mapX", mapX)
        searchToMapIntent.putExtra("mapY", mapY)
        searchToMapIntent.putExtra("name", name)
        searchToMapIntent.putExtra("address", address)
        Log.d("goBackToMap", "goBackToMap: $mapX, $mapY")

//        if (!(application as KyleMaps).isTestMode) {
        finish()
        startActivity(searchToMapIntent)
//        }
    }

    private fun updateMapPosition(place: Place) {
        mapX = place.x
        mapY = place.y
        name = place.place_name
        address = place.address_name
        Log.d("goBackToMap", "updateMapPosition: $mapX, $mapY")
    }
}
