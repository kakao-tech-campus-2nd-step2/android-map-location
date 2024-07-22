package campus.tech.kakao.map.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.viewmodel.MapItem
import campus.tech.kakao.map.viewmodel.MapViewModel
import campus.tech.kakao.map.R

class MainActivity : AppCompatActivity(), SearchResultAdapter.OnItemClickListener,
    KeywordAdapter.OnKeywordRemoveListener {

    lateinit var mapViewModel: MapViewModel             // 유닛 테스트를 위해 public으로 변경
    private lateinit var etKeywords: EditText
    private lateinit var rvSearchResult: RecyclerView
    private lateinit var rvKeywords: RecyclerView
    private lateinit var tvNoResults: TextView
    private lateinit var ivClear: ImageView

    private val searchResultAdapter = SearchResultAdapter(this)
    private val keywordAdapter = KeywordAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etKeywords = findViewById(R.id.etKeywords)
        rvSearchResult = findViewById(R.id.rvSearchResult)
        rvKeywords = findViewById(R.id.rvKeywords)
        tvNoResults = findViewById(R.id.tvNoResults)
        ivClear = findViewById(R.id.ivClear)

        rvSearchResult.layoutManager = LinearLayoutManager(this)
        rvSearchResult.adapter = searchResultAdapter

        rvKeywords.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvKeywords.adapter = keywordAdapter

        mapViewModel = ViewModelProvider(this).get(MapViewModel::class.java)

        etKeywords.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val keyword = s.toString()
                if (keyword.isNotEmpty()) {
                    mapViewModel.searchPlaces(keyword)
                } else {
                    searchResultAdapter.submitList(emptyList())
                    tvNoResults.visibility = TextView.VISIBLE
                    rvSearchResult.visibility = RecyclerView.GONE
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        mapViewModel.searchResults.observe(this, Observer { results ->
            if (results.isEmpty()) {
                tvNoResults.visibility = TextView.VISIBLE
                rvSearchResult.visibility = RecyclerView.GONE
            } else {
                tvNoResults.visibility = TextView.GONE
                rvSearchResult.visibility = RecyclerView.VISIBLE
                searchResultAdapter.submitList(results)
            }
        })

        mapViewModel.errorMessage.observe(this, Observer { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })

        ivClear.setOnClickListener {
            etKeywords.text.clear()
        }

        loadKeywords()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@MainActivity, MapActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
    }

    override fun onItemClick(item: MapItem) {
        keywordAdapter.addKeyword(item.name)
        saveKeywords()
        // 검색어 결과 항목 클릭시 검색창 자동완성 (요구된 기능 외 추가 기능, 필요시 주석 제거)
        // etKeywords.setText(item.name)
        // mapViewModel.searchPlaces(item.name)

        val intent = Intent(this, MapActivity::class.java).apply {
            putExtra("name", item.name)
            putExtra("address", item.address)
            putExtra("longitude", item.longitude)
            putExtra("latitude", item.latitude)
            //Log.d("putLatitude: ", item.latitude.toString())
            //Log.d("putLongitude: ", item.longitude.toString())
        }
        startActivity(intent)
    }

    override fun onKeywordRemove(keyword: String) {
        saveKeywords()
    }

    override fun onKeywordClick(keyword: String) {
        etKeywords.setText(keyword)
        mapViewModel.searchPlaces(keyword)
    }

    private fun loadKeywords() {
        val sharedPreferences = getSharedPreferences("keywords", MODE_PRIVATE)
        val keywords = sharedPreferences.getStringSet("keywords", setOf())?.toMutableList() ?: mutableListOf()
        keywordAdapter.submitList(keywords)
    }

    private fun saveKeywords() {
        val sharedPreferences = getSharedPreferences("keywords", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putStringSet("keywords", keywordAdapter.currentKeywords.toSet())
        editor.apply()
    }
}
