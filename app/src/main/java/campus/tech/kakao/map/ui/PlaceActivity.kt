package campus.tech.kakao.map.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import campus.tech.kakao.map.domain.PlaceDataModel
import campus.tech.kakao.map.data.PlaceDatabaseAccess
import campus.tech.kakao.map.data.PlaceRepository
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.SearchLayoutBinding

class PlaceActivity : AppCompatActivity() {
    lateinit var binding: SearchLayoutBinding
    lateinit var placeAdapter: PlaceRecyclerViewAdapter
    private lateinit var searchAdapter: SearchRecyclerViewAdapter
    var searchDatabaseAccess = PlaceDatabaseAccess(this, "Search.db")

    lateinit var placeRepository: PlaceRepository

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.search_layout)

        val searchList: MutableList<PlaceDataModel> = searchDatabaseAccess.getAllPlace()
        val keywordList: MutableList<PlaceDataModel> = mutableListOf()

        placeRepository = PlaceRepository()

        // Search 어댑터
        searchAdapter = searchRecyclerViewAdapter(searchList)
        binding.rvSearchList.adapter = searchAdapter
        binding.rvSearchList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Place 어댑터
        placeAdapter = placeRecyclerViewAdapter(keywordList, searchList)
        binding.rvPlaceList.adapter = placeAdapter
        binding.rvPlaceList.layoutManager = LinearLayoutManager(this)

        controlPlaceVisibility(keywordList)
        controlSearchVisibility(searchList)

        binding.btnErase.setOnClickListener {
            binding.etSearch.setText("")
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val keyword = s.toString()
                Log.d("API response", "$keyword")
                searchPlace(keyword)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun searchPlace(keyword: String) {
        placeRepository.searchPlace(keyword,
            onSuccess = { keywordList ->
                placeAdapter.updateData(keywordList)
                placeAdapter.notifyDataSetChanged()
                controlPlaceVisibility(keywordList)
            },
            onFailure = { throwable ->
                Log.w("API response", "Failure: $throwable")
            }
        )
    }

    private fun placeRecyclerViewAdapter(placeList: MutableList<PlaceDataModel>, searchList: MutableList<PlaceDataModel>) =
        PlaceRecyclerViewAdapter(
            placeList,
            onItemClick = { place ->
                // 장소 목록 선택 시, 검색어 기록 저장
                if (place in searchList) {
                    removePlaceRecord(searchList, place)
                }
                addPlaceRecord(searchList, place)
                controlSearchVisibility(searchList)

                // 장소 목록 선택 시, 해당 항목의 위치를 지도에 표시 -> !!!!!!
                Log.d("place", "${place.x}, ${place.y}")
                val mapIntent = Intent(this, MapActivity::class.java).apply {
                    putExtra("name", place.name)
                    putExtra("address", place.address)
                    putExtra("category", place.category)
                    putExtra("latitude", place.x)
                    putExtra("longitude", place.y)
                }
                startActivity(mapIntent)
            }
        )

    private fun searchRecyclerViewAdapter(searchList: MutableList<PlaceDataModel>) =
        SearchRecyclerViewAdapter(
            searchList,
            // 저장 목록 선택 시, 검색칸에 장소명 표시
            onItemClick = { place ->
                binding.etSearch.setText(place.name)
                binding.etSearch.setSelection(place.name.length)
            },
            // X 선택 시, 저장 목록에서 삭제
            onCloseButtonClick = { place ->
                removePlaceRecord(searchList, place)
                controlSearchVisibility(searchList)
            }
        )

    // 검색 저장 기록 조작
    fun addPlaceRecord(searchList: MutableList<PlaceDataModel>, place: PlaceDataModel) {
        searchList.add(place)
        searchDatabaseAccess.insertPlace(place)
        searchAdapter.notifyDataSetChanged()
    }

    fun removePlaceRecord(searchList: MutableList<PlaceDataModel>, place: PlaceDataModel) {
        val index = searchList.indexOf(place)
        searchList.removeAt(index)
        searchDatabaseAccess.deletePlace(place.name)
        searchAdapter.notifyDataSetChanged()
    }

    // visibility 조작
    fun controlPlaceVisibility(placeList: List<PlaceDataModel>) {
        if (placeList.isEmpty()) {
            binding.rvPlaceList.visibility = View.INVISIBLE
            binding.tvNoData.visibility = View.VISIBLE
        }
        else {
            binding.rvPlaceList.visibility = View.VISIBLE
            binding.tvNoData.visibility = View.GONE
        }
    }

    fun controlSearchVisibility(searchList: List<PlaceDataModel>) {
        if (searchList.isEmpty()) {
            binding.rvSearchList.visibility = View.GONE
        }
        else {
            binding.rvSearchList.visibility = View.VISIBLE
        }
    }
}