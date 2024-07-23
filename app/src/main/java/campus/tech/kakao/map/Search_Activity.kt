package campus.tech.kakao.map

import android.content.ContentValues
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kakao.sdk.common.KakaoSdk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Search_Activity : AppCompatActivity() {
    //Binding만 추가하면 됨
    private lateinit var searchView: SearchView
    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var savedSearchRecyclerView: RecyclerView
    private lateinit var searchResultAdapter: PlaceAdapter
    private lateinit var savedSearchAdapter: SavedSearchAdapter
    private lateinit var databaseHelper: MyDatabaseHelper
    private lateinit var kakaoApiService: KakaoApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KakaoSdk.init(this, "e6a7c826ae7a55df129b8be2c636e213")
        setContentView(R.layout.activity_search)
        kakaoApiService = createKakaoApiService()

        initViews()
        databaseHelper = MyDatabaseHelper(this)

        initAdapters()
        setupRecyclerViews()
        setupSearchView()
        //searchAndDisplayResults("")
    }
    private fun createKakaoApiService(): KakaoApiService {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "KakaoAK 13c6b4e1c003d0f42b3b07888391c355")
                    .build()
                chain.proceed(request)
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(KakaoApiService::class.java)
    }

    private fun initViews() {
        searchView = findViewById(R.id.search_text)
        searchRecyclerView = findViewById(R.id.RecyclerVer)
        savedSearchRecyclerView = findViewById(R.id.recyclerHor)
    }


    private fun initAdapters() {
        searchResultAdapter = PlaceAdapter(emptyList())
        savedSearchAdapter = SavedSearchAdapter(this, emptyList()) { searchText ->
            searchAndDisplayResults(searchText)
        }
    }

    private fun setupRecyclerViews() {
        searchRecyclerView.layoutManager = LinearLayoutManager(this)
        savedSearchRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        searchRecyclerView.adapter = searchResultAdapter
        savedSearchRecyclerView.adapter = savedSearchAdapter
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    searchAndDisplayResults(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    searchAndDisplayResults("")
                } else {
                    searchAndDisplayResults(newText)
                }
                return true
            }
        })
    }


    private fun searchAndDisplayResults(searchText: String) {
        if (searchText.isBlank()) {
            searchRecyclerView.visibility = RecyclerView.GONE
            return
        }

        lifecycleScope.launch(Dispatchers.Main) {
            try {
                val response = kakaoApiService.searchAddress(searchText)
                val documents = response.documents ?: emptyList()

                val places = documents.map { document ->
                    mapOf(
                        MapContract.COLUMN_NAME to (document.placeName ?: "No Name"),
                        MapContract.COLUMN_ADDRESS to (document.addressName ?: "No Address"),
                        MapContract.COLUMN_CATEGORY to (document.categoryName ?: "No Category")
                    )
                }

                if (places.isEmpty()) {
                    searchRecyclerView.visibility = RecyclerView.GONE
                } else {
                    searchResultAdapter.updateData(places)
                    searchRecyclerView.visibility = RecyclerView.VISIBLE
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }



    private fun getContentValues(place: Map<String, String>): ContentValues {
        return ContentValues().apply {
            put(MapContract.COLUMN_NAME, place[MapContract.COLUMN_NAME])
            put(MapContract.COLUMN_ADDRESS, place[MapContract.COLUMN_ADDRESS])
            put(MapContract.COLUMN_CATEGORY, place[MapContract.COLUMN_CATEGORY])
        }
    }

    inner class PlaceAdapter(private var data: List<Map<String, String>>) :
        RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_place_item, parent, false)
            return PlaceViewHolder(view)
        }

        override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
            val place = data[position]
            holder.bind(place)
        }

        override fun getItemCount(): Int = data.size

        fun updateData(newData: List<Map<String, String>>) {
            data = newData
            notifyDataSetChanged() //성능 안좋음 고칠 필요가 있음
        }

        inner class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val nameTextView: TextView = itemView.findViewById(R.id.name)
            private val addressTextView: TextView = itemView.findViewById(R.id.place)
            private val categoryTextView: TextView = itemView.findViewById(R.id.category)

            fun bind(place: Map<String, String>) {
                nameTextView.text = place[MapContract.COLUMN_NAME] ?: "No Name"
                addressTextView.text = place[MapContract.COLUMN_ADDRESS] ?: "No Address"
                categoryTextView.text = place[MapContract.COLUMN_CATEGORY] ?: "No Category"
            }
        }
    }
}






