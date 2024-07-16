package campus.tech.kakao.map

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import campus.tech.kakao.map.databinding.ActivityMainBinding
import campus.tech.kakao.map.BuildConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var HistoryDBHelper : HistoryDBHelper
    private lateinit var HistoryDB : SQLiteDatabase
    private lateinit var adapter: RecycleAdapter
    private lateinit var horadapter : HorRecycleAdapter
    private val listItems = arrayListOf<ListLayout>()

    companion object {
        const val BASE_URL = "https://dapi.kakao.com/"
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val Delete = binding.Delete
        val SearchText = binding.SearchText

        Delete.setOnClickListener{
            SearchText.setText(null)
        }

        HistoryDBHelper = HistoryDBHelper(this,"history.db",null,2)

        searchKeyword("카페")

        SetupView()
    }

    fun SetupView() {
        val Search = binding.SearchText
        val NoSearchText = binding.NoSearchText
        val RecyclerView = binding.RecyclerView
        val HolRecyclerView = binding.HorRecyclerView

        adapter = RecycleAdapter(){ name ->
            if (SearchHistory(name) == 0) {
                insertHistory(name)
                SubAllHistory()
            }
        }

        horadapter = HorRecycleAdapter(){ name ->
            DeleteItem(name)
        }
        RecyclerView.adapter = adapter
        RecyclerView.layoutManager = LinearLayoutManager(this)

        HolRecyclerView.adapter = horadapter
        HolRecyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL, false)

        SubAllHistory()

        Search.addTextChangedListener {
            val searchText = it.toString().trim()
            if (searchText.isEmpty()) {
                NoSearchText.visibility = View.VISIBLE
                RecyclerView.visibility = View.INVISIBLE
            } else {
                NoSearchText.visibility = View.INVISIBLE
                RecyclerView.visibility = View.VISIBLE
                searchKeyword(searchText)
            }
        }
    }

    private fun searchKeyword(keyword: String) {
        Log.d(TAG, "searchKeyword: $keyword")

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(KakaoAPI::class.java)
        val call = api.getSearchKeyword("KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}", keyword)

        call.enqueue(object: Callback<ResultSearch> {
            override fun onResponse(
                call: Call<ResultSearch>,
                response: Response<ResultSearch>
            ) {
                if (response.isSuccessful) {
                    searchPlaceAPI(response.body())
                    adapter.submitList(listItems)
                }
            }

            override fun onFailure(call: Call<ResultSearch>, t: Throwable) {
            }
        })
    }

    private fun searchPlaceAPI(searchResult: ResultSearch?) {
        if (!searchResult?.documents.isNullOrEmpty()) {
            listItems.clear()
            for (document in searchResult!!.documents) {
                val item = ListLayout(document.place_name, document.road_address_name, document.category_group_name)
                listItems.add(item)
            }
        }
    }

    private fun SearchHistory(name: String): Int {
        val selection = "${HistoryEntry.COLUMN_NAME} = ?"
        val selectionArgs = arrayOf(name)
        HistoryDB = HistoryDBHelper.readableDatabase
        val cursor = HistoryDB.query(
            HistoryEntry.TABLE_NAME,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        return if (cursor.moveToFirst()) {
            1
        } else {
            0
        }
    }

    private fun SubAllHistory() {
        HistoryDB = HistoryDBHelper.readableDatabase
        val cursor = HistoryDB.query(
            HistoryEntry.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )
        horadapter.SubmitCursor(cursor)
    }

    private fun insertHistory(name: String) {
        HistoryDB = HistoryDBHelper.writableDatabase
        val values = ContentValues()
        values.put(HistoryEntry.COLUMN_NAME, name)
        HistoryDB.insert(HistoryEntry.TABLE_NAME, null, values)
    }

    private fun DeleteItem(name: String) {
        val selection = "${HistoryEntry.COLUMN_NAME} = ?"
        val selectionArgs = arrayOf(name)

        HistoryDB = HistoryDBHelper.writableDatabase

        val deleteRows = HistoryDB.delete(
            HistoryEntry.TABLE_NAME,
            selection,
            selectionArgs
        )

        if (deleteRows > 0) {
            SubAllHistory()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
