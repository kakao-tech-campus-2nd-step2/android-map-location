package campus.tech.kakao.map.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import campus.tech.kakao.map.viewmodel.PlaceViewModel
import campus.tech.kakao.map.data.db.entity.Place
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityMainBinding
import campus.tech.kakao.map.viewmodel.LogViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val placeViewModel: PlaceViewModel by viewModels()
    private val logViewModel: LogViewModel by viewModels()
    private lateinit var resultAdapter: RecyclerViewAdapter
    private lateinit var tapAdapter: TapViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupRecyclerViewOfResult()
        setupRecyclerViewOfLog()
        setupEditTextListeners()
        observeLogListChanges()
    }

    private fun setupBinding(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = placeViewModel
        binding.lifecycleOwner = this
    }
    private fun setupRecyclerViewOfResult(){
        resultAdapter = RecyclerViewAdapter {
            logViewModel.insertLog(it)
            placeViewModel.saveLastLocation(it)
            moveMapView(it)
        }

        binding.recyclerView.apply {
            adapter = resultAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }
    private fun setupRecyclerViewOfLog(){
        tapAdapter = TapViewAdapter {
            logViewModel.deleteLog(it)
        }

        binding.tabRecyclerview.apply {
            adapter = tapAdapter
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
        }
    }
    private fun setupEditTextListeners(){
        // EditView 입력창
        binding.input.doAfterTextChanged { userInput ->
            handleTextChanged(userInput.toString())
        }

        // EditView 내 closeButton
        binding.closeButton.setOnClickListener {
            binding.input.text.clear()
            placeViewModel.clearPlaceList()
        }
    }
    private fun handleTextChanged(query: String){
        // ViewModel에 콜백전달 -> 순서부여
        placeViewModel.callResultList(query){
            observePlaceListChanges()
        }
    }
    private fun observePlaceListChanges(){
        updatePlaceList()
        showPlaceList()
    }
    private fun updatePlaceList(){
        placeViewModel.placeList.observe(this) { list ->
            resultAdapter.submitList(list)
            resultAdapter.notifyDataSetChanged()
        }
    }
    private fun showPlaceList(){
        placeViewModel.placeListVisible.observe(this) {
            binding.recyclerView.isVisible = it
            binding.noResultTextview.isVisible = !it
        }
    }
    private fun observeLogListChanges(){
        updateLogList()
        showLogList()
    }
    private fun updateLogList(){
        logViewModel.logList.observe(this) {
            tapAdapter.submitList(it)
            tapAdapter.notifyDataSetChanged()
        }
    }
    private fun showLogList(){
        logViewModel.tabViewVisible.observe(this) {
            binding.tabRecyclerview.isVisible = it
        }
    }
    private fun moveMapView(place: Place) {
        val intent = Intent(this, MapViewActivity::class.java)
        intent.putExtra(EXTRA_PLACE_NAME, place.name)
        intent.putExtra(EXTRA_PLACE_ADDR, place.location)
        intent.putExtra(EXTRA_PLACE_X, place.x)
        intent.putExtra(EXTRA_PLACE_Y, place.y)
        startActivity(intent)
    }

    companion object {
        const val EXTRA_PLACE_NAME = "PLACE_NAME"
        const val EXTRA_PLACE_ADDR = "PLACE_LOCATION"
        const val EXTRA_PLACE_X = "PLACE_X"
        const val EXTRA_PLACE_Y = "PLACE_Y"
    }
}

