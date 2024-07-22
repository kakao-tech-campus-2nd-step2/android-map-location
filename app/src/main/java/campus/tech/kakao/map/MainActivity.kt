package campus.tech.kakao.map

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var adapter: Adapter

    lateinit var tvNoResult: TextView
    lateinit var llSave: LinearLayoutCompat
    lateinit var hScrollView: HorizontalScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        키 해시 받기
//        var keyHash = Utility.getKeyHash(this)
//        Log.d("keyHash", keyHash)


        val etSearch = findViewById<EditText>(R.id.etSearch)
        tvNoResult = findViewById(R.id.tvNoResult)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val btnClose = findViewById<Button>(R.id.btnClose)
        llSave = findViewById(R.id.llSave)
        hScrollView = findViewById(R.id.hScrollView)

        adapter = Adapter(mutableListOf())

        recyclerView.adapter = adapter

        tvNoResult.visibility = TextView.VISIBLE

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val search = s.toString()
                if (search.isEmpty()) {
                    showNoResults()
                } else {
                    searchKeyword(search)  // 키워드
                    CategoryGroupCode.categoryMap[search]?.let { categoryCode ->
                        searchCategory(categoryCode)
                    }  // 카테고리
                }
            }
        })

        adapter.setOnItemClickListener(object : Adapter.OnItemClickListener {
            override fun onItemClick(name: String, address: String, latitude: String, longitude: String) {
                if (isProfileInSearchSave(name)) {
                    removeSavedItem(name)
                }
                addSavedItem(name)
                val intent = Intent(this@MainActivity, MapActivity::class.java).apply {
                    putExtra("name", name)
                    putExtra("address", address)
                    putExtra("latitude", latitude)
                    putExtra("longitude", longitude)
                }
                startActivity(intent)
            }
        })

        btnClose.setOnClickListener {
            etSearch.text.clear()
        }
        loadSavedItems()
    }

    // 키워드로 검색
    fun searchKeyword(query: String) {
        Network.searchKeyword(query, object : Callback<KakaoResponse> {
            override fun onResponse(call: Call<KakaoResponse>, response: Response<KakaoResponse>) {
                if (response.isSuccessful) {
                    searchProfiles(response.body())
                } else {
                    Toast.makeText(applicationContext, "응답 실패", Toast.LENGTH_SHORT).show()
                    Log.e("MainActivity", "응답 실패")
                }
            }

            override fun onFailure(call: Call<KakaoResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "요청 실패: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("MainActivity", "요청 실패", t)
            }
        })
    }

    // 카테고리로 검색
    fun searchCategory(categoryGroupCode: String) {
        Network.searchCategory(categoryGroupCode, object : Callback<KakaoResponse> {
            override fun onResponse(call: Call<KakaoResponse>, response: Response<KakaoResponse>) {
                if (response.isSuccessful) {
                    searchProfiles(response.body())
                } else {
                    Toast.makeText(applicationContext, "응답 실패", Toast.LENGTH_SHORT).show()
                    Log.e("MainActivity", "응답 실패")
                }
            }

            override fun onFailure(call: Call<KakaoResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "요청 실패: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("MainActivity", "요청 실패", t)
            }
        })
    }

    fun searchProfiles(searchResult: KakaoResponse?) {
        searchResult?.documents?.let { documents ->
            if (documents.isEmpty()) {
                showNoResults()
            } else {
                val profiles = documents.map { it.toProfile()}
                adapter.updateProfiles(profiles)
                tvNoResult.visibility = View.GONE
            }
        } ?: showNoResults()
    }

    fun Document.toProfile(): Profile {
        return Profile(this.name, this.address, this.type, this.latitude, this.longitude)
    }

    fun showNoResults() {
        tvNoResult.visibility = View.VISIBLE
        adapter.updateProfiles(emptyList())
    }

    override fun onPause() {
        super.onPause()
        saveSavedItems()
    }

    fun saveSavedItems() {
        val sharedPreferences = getSharedPreferences("SavedItems", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val savedNames = JSONArray()
        for (i in 0 until llSave.childCount) {
            val savedView = llSave.getChildAt(i) as? ConstraintLayout
            val tvSaveName = savedView?.findViewById<TextView>(R.id.tvSaveName)
            if (tvSaveName != null) {
                savedNames.put(tvSaveName.text.toString())
            }
        }
        editor.putString("savedNames", savedNames.toString())
        editor.apply()
    }

    fun loadSavedItems() {
        val sharedPreferences = getSharedPreferences("SavedItems", MODE_PRIVATE)
        val savedNamesString = sharedPreferences.getString("savedNames", "[]")
        val savedNames = JSONArray(savedNamesString)
        for (i in 0 until savedNames.length()) {
            val name = savedNames.getString(i)
            addSavedItem(name)
        }
    }


    fun addSavedItem(name: String) {
        val savedView = LayoutInflater.from(this)
            .inflate(R.layout.search_save, llSave, false) as ConstraintLayout

        val tvSaveName = savedView.findViewById<TextView>(R.id.tvSaveName)
        val ivDelete = savedView.findViewById<ImageView>(R.id.ivDelete)

        tvSaveName.text = name

        // 저장된 검색어를 누르면 검색어가 입력됨
        val etSearch = findViewById<EditText>(R.id.etSearch)
        tvSaveName.setOnClickListener {
            etSearch.setText(name)
        }

        ivDelete.setOnClickListener {
            llSave.removeView(savedView)
        }

        llSave.addView(savedView)
        hScrollView.visibility = View.VISIBLE
        scrollToEndOfSearchSave()
    }

    fun removeSavedItem(name: String) {
        for (i in 0 until llSave.childCount) {
            val savedView = llSave.getChildAt(i) as? ConstraintLayout
            val tvSaveName = savedView?.findViewById<TextView>(R.id.tvSaveName)
            if (tvSaveName?.text.toString() == name) {
                llSave.removeViewAt(i)
                break
            }
        }
    }

    fun isProfileInSearchSave(name: String): Boolean {
        for (i in 0 until llSave.childCount) {
            val savedView = llSave.getChildAt(i) as? ConstraintLayout
            val tvSaveName = savedView?.findViewById<TextView>(R.id.tvSaveName)
            if (tvSaveName?.text.toString() == name) {
                return true
            }
        }
        return false
    }

    fun scrollToEndOfSearchSave() {
        hScrollView.post {
            hScrollView.fullScroll(View.FOCUS_RIGHT)
        }
    }
}
