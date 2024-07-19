package campus.tech.kakao.map

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat.performHapticFeedback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import campus.tech.kakao.map.databinding.ActivitySearchPlaceBinding

class SearchPlaceActivity : AppCompatActivity() {


    private lateinit var binding: ActivitySearchPlaceBinding
    private lateinit var viewModel: MyViewModel
    private lateinit var placeAdapter: PlaceAdapter
    private lateinit var savedSearchAdapter: SavedSearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_place)
        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        binding.viewModel = viewModel
        val dbManager = DatabaseManager(context = this)
        viewModel.isIntent.value = false //인텐트를 위한 조정

        //Place 리사이클러뷰 설정
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        placeAdapter = PlaceAdapter(emptyList(), viewModel)
        binding.recyclerView.adapter = placeAdapter

        //savedSearch 저장된 검색어 설정
        val savedSearch = binding.savedSearch
        savedSearch.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        savedSearchAdapter = SavedSearchAdapter(emptyList(),viewModel)
        binding.savedSearch.adapter = savedSearchAdapter

        viewModel.updateSavedSearch(dbManager)


        //-----viewModel observe-----------------------------------------
        val activity = this
        with(viewModel){

            //PlaceAdapter
            itemClick.observe(activity, Observer {  //Place 클릭 이벤트
                dbManager.insertSavedPlace(it.id, it.name)
                viewModel.updateSavedSearch(dbManager)

                //sharedPreference를 이용해서 name,address,latitude,longitude 저장하기
                val sharedPreferences = getSharedPreferences("PlacePreferences", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("name", it.name)
                editor.putString("address", it.address)
                editor.putString("longitude", it.longitude)
                editor.putString("latitude", it.latitude)
                editor.apply()

                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
            })

            //SavedSearchAdapter
            closeClick.observe(activity, Observer { //closeIcon 클릭 이벤트
                dbManager.deleteSavedSearch(it.id)
                viewModel.updateSavedSearch(dbManager)
            })
            nameClick.observe(activity, Observer { //name 클릭 이벤트
                binding.search.setText(it.name)
                viewModel.searchText.value = it.name
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
                if (it == " "){ //searchText가 비어있다면 화면에서도 지우기
                    binding.search.text.clear()
                    placeAdapter.updateData(listOf<Place>())
                }
                else viewModel.searchPlaces(it) //텍스트가 있다면 검색
            })

        }




    } //onCreate

}
