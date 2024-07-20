package campus.tech.kakao.map

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainModel: MainModel
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        mainModel = MainModel(application as MyApplication)

        val viewModelFactory = MainViewModelFactory(application as MyApplication, mainModel)
        mainViewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        binding.viewModel = mainViewModel
        binding.lifecycleOwner = this

        val resultAdapter = RecyclerViewAdapter {
            mainViewModel.resultItemClickListener(it)
            moveMapView(it)
        }
        binding.recyclerView.apply {
            adapter = resultAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        mainViewModel.placeList.observe(this) { list ->
            if (list.isEmpty()) {
                binding.noResultTextview.isVisible = true
                binding.recyclerView.isVisible = false
            } else {
                binding.noResultTextview.isVisible = false
                binding.recyclerView.isVisible = true
                resultAdapter.submitList(list)
            }
        }


        val tapAdapter = TapViewAdapter {
            mainViewModel.deleteLogClickListner(it)
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

        binding.input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 미사용
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 미사용
            }

            override fun afterTextChanged(s: Editable?) {
                mainViewModel.callResultList(s.toString())
            }
        })

        binding.closeButton.setOnClickListener {
            binding.input.setText("")
            mainViewModel.inputCloseButtonClickListener()
        }
    }

    private fun moveMapView(place: Place) {
        val intent = Intent(this, MapViewActivity::class.java)
        intent.putExtra("PLACE_NAME", place.name)
        intent.putExtra("PLACE_LOCATION", place.location)
        intent.putExtra("PLACE_X", place.x)
        intent.putExtra("PLACE_Y", place.y)
        startActivity(intent)
    }

    private fun saveLastLocation(context: Context) {
        val lastLocation = mainViewModel.callLogList().lastOrNull()

        val sharedPreferences = context.getSharedPreferences("LastLocation", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("PLACE_X", lastLocation?.x.toString())
            putString("PLACE_Y", lastLocation?.y.toString())
            apply()
        }
    }
    override fun onStop() {
        super.onStop()
        saveLastLocation(this)
    }
}

