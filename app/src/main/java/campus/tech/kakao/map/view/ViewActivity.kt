package campus.tech.kakao.map.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.PlaceApplication
import campus.tech.kakao.map.R
import campus.tech.kakao.map.domain.model.ResultSearchKeyword
import campus.tech.kakao.map.databinding.ActivityMainBinding
import campus.tech.kakao.map.view.adapter.SearchedPlaceAdapter
import campus.tech.kakao.map.view.adapter.LogAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import campus.tech.kakao.map.data.net.RetrofitApiClient
import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.util.PlaceMapper
import com.kakao.sdk.common.util.Utility

class ViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var searchedPlaceAdapter: SearchedPlaceAdapter
    private lateinit var logAdapter: LogAdapter
    private lateinit var viewModel: PlaceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        initViewModel()
        initBinding()
        setupRecyclerViews()
        observeViewModel()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, PlaceViewModel.provideFactory(application as PlaceApplication))
            .get(PlaceViewModel::class.java)
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun setupRecyclerViews() {
        setupSearchedPlaceRecyclerView()
        setupLogRecyclerView()
    }

    private fun setupSearchedPlaceRecyclerView() {
        val searchedPlaceRecyclerView = binding.recyclerPlace
        searchedPlaceAdapter = SearchedPlaceAdapter { place -> viewModel.updateLogs(place) }

        searchedPlaceRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ViewActivity)
            adapter = searchedPlaceAdapter
        }
    }

    private fun setupLogRecyclerView() {
        val logRecyclerView = binding.recyclerLog
        logAdapter = LogAdapter { id -> viewModel.removeLog(id) }
        logAdapter.submitList(viewModel.getLogs())

        logRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ViewActivity, RecyclerView.HORIZONTAL, false)
            adapter = logAdapter
        }
    }

    private fun observeViewModel() {

        viewModel.places.observe(this, Observer { places ->
            updateSearchedPlaceList(places)
            binding.tvHelpMessage.visibility=
                if (places.isEmpty()) View.VISIBLE else View.GONE
        })

        viewModel.logList.observe(this, Observer { logList ->
            logAdapter.submitList(logList)
        })
    }

    private fun updateSearchedPlaceList(places: List<Place>) {
        searchedPlaceAdapter.submitList(places)
    }
}
