package campus.tech.kakao.map.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import campus.tech.kakao.map.viewmodel.MainViewModel
import campus.tech.kakao.map.viewmodel.MainViewModelFactory
import campus.tech.kakao.map.base.MyApplication
import campus.tech.kakao.map.data.db.entity.Place
import campus.tech.kakao.map.repository.PlaceRepository
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityMainBinding
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_PLACE_NAME = "PLACE_NAME"
        const val EXTRA_PLACE_ADDR = "PLACE_LOCATION"
        const val EXTRA_PLACE_X = "PLACE_X"
        const val EXTRA_PLACE_Y = "PLACE_Y"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var placeRepository: PlaceRepository
    private lateinit var mainViewModel: MainViewModel
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        placeRepository = PlaceRepository(application as MyApplication)

        val viewModelFactory = MainViewModelFactory(application as MyApplication, placeRepository)
        mainViewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        binding.viewModel = mainViewModel
        binding.lifecycleOwner = this

        observeInputTextChanges()

        val resultAdapter = RecyclerViewAdapter {
            mainViewModel.resultItemClickListener(it)
            mainViewModel.saveLastLocation(it)
            moveMapView(it)
        }
        binding.recyclerView.apply {
            adapter = resultAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        mainViewModel.placeList.observe(this) { list ->
            resultAdapter.submitList(list)
            resultAdapter.notifyDataSetChanged()
        }

        mainViewModel.placeListVisible.observe(this) {
            binding.recyclerView.isVisible = it
            binding.noResultTextview.isVisible = !it
        }

        val tapAdapter = TapViewAdapter {
            mainViewModel.deleteLog(it)
        }
        binding.tabRecyclerview.apply {
            adapter = tapAdapter
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
        }

        mainViewModel.logList.observe(this) {
            tapAdapter.submitList(it)
            tapAdapter.notifyDataSetChanged()
        }

        mainViewModel.tabViewVisible.observe(this) {
            binding.tabRecyclerview.isVisible = it
        }

        binding.closeButton.setOnClickListener {
            binding.input.text.clear()
            mainViewModel.closeButtonClickListener()
        }
    }

    private fun observeInputTextChanges(){
        val inputChangeObservable = binding.input.textChanges()
        val disposable = inputChangeObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { text ->
                mainViewModel.callResultList(text.toString())
                mainViewModel.showPlaceList()
            }
        disposables.add(disposable)
    }
    private fun moveMapView(place: Place) {
        val intent = Intent(this, MapViewActivity::class.java)
        intent.putExtra(EXTRA_PLACE_NAME, place.name)
        intent.putExtra(EXTRA_PLACE_ADDR, place.location)
        intent.putExtra(EXTRA_PLACE_X, place.x)
        intent.putExtra(EXTRA_PLACE_Y, place.y)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }
}

