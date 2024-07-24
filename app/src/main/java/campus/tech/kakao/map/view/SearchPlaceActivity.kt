package campus.tech.kakao.map.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import campus.tech.kakao.map.viewmodel.MyViewModel
import campus.tech.kakao.map.model.data.Place
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivitySearchPlaceBinding
import campus.tech.kakao.map.model.repository.MyRepository
import campus.tech.kakao.map.viewmodel.MyViewModelFactory

class SearchPlaceActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchPlaceBinding
    private val viewModel: MyViewModel by viewModels {
        MyViewModelFactory(this, MyRepository(applicationContext))
    }
    private lateinit var placeAdapter: PlaceAdapter
    private lateinit var savedSearchAdapter: SavedSearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_place)
        binding.viewModel = viewModel
        viewModel.isIntent.value = false //인텐트를 위한 조정

        //Place 리사이클러뷰 설정
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        placeAdapter = viewModel.vmPlaceAdapter
        binding.recyclerView.adapter = placeAdapter

        //savedSearch 저장된 검색어 설정
        val savedSearch = binding.savedSearch
        savedSearch.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        savedSearchAdapter = viewModel.vmSavedSearchAdapter
        binding.savedSearch.adapter = savedSearchAdapter

        viewModel.updateSavedSearch()

        //-----viewModel observe-----------------------------------------
        val activity = this
        with(viewModel) {

            //PlaceAdapter
            itemClick.observe(activity, Observer { place ->  //Place 클릭 했을 때
//                setSharedPreferences(place)
                finish()
            })

            //SavedSearchAdapter
            closeClick.observe(activity, Observer { //close 클릭 이벤트
                //필요없음
            })
            nameClick.observe(activity, Observer { //name 클릭 이벤트
                binding.search.setText(it.name)
            })

            //Place 리사이클러뷰 업데이트 관찰
            placeAdapterUpdateData.observe(activity, Observer {
                placeAdapter.updateData(it)
            })

            //savedSearch 저장된 검색어 관찰
            savedSearchAdapterUpdateData.observe(activity, Observer {
                savedSearchAdapter.updateData(it)
            })

            //editText에서 변경 감지
            searchText.observe(activity, Observer {
                if (it == " ") { //searchText가 비어있다면 화면에서도 지우기
                    binding.search.text.clear()
                    placeAdapter.updateData(listOf<Place>())
                } else {
                    this.searchPlace(it) //텍스트가 있다면 검색
                }
            })
        } //with(viewModel)
    } //onCreate

//    private fun setSharedPreferences(place: Place) {
//        val sharedPreferences = getSharedPreferences("PlacePreferences", MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        editor.putString("name", place.name)
//        editor.putString("address", place.address)
//        editor.putString("latitude", place.latitude)
//        editor.putString("longitude", place.longitude)
//        editor.apply()
//    }
}
