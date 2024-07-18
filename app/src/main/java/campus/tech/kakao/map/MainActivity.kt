package campus.tech.kakao.map

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

    private lateinit var input: EditText
    private lateinit var researchCloseButton: ImageView
    private lateinit var noResultTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var tabRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        mainModel = MainModel(application as MyApplication)

        val viewModelFactory = MainViewModelFactory(application as MyApplication, mainModel)
        val mainViewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        binding.viewModel = mainViewModel
        binding.lifecycleOwner = this

        input = binding.input
        researchCloseButton = binding.closeButton
        noResultTextView = binding.noResultTextview
        recyclerView = binding.recyclerView
        tabRecyclerView = binding.tabRecyclerview

        val resultAdapter = RecyclerViewAdapter {
            mainViewModel.resultItemClickListener(it)
        }
        recyclerView.adapter = resultAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        mainViewModel.placeList.observe(this) { list ->
            if (list.isEmpty()) {
                noResultTextView.isVisible = true
                recyclerView.isGone = true
            } else {
                noResultTextView.isGone = true
                recyclerView.isVisible = true
                resultAdapter.submitList(list)
            }
        }


        val tapAdapter = TapViewAdapter {
            mainViewModel.deleteLogClickListner(it)
        }
        tabRecyclerView.adapter = tapAdapter
        tabRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mainViewModel.logList.observe(this) {
            tapAdapter.submitList(it)
            tapAdapter.notifyDataSetChanged()
        }
        mainViewModel.tabViewVisible.observe(this) {
            tabRecyclerView.isVisible = it
        }

        input.addTextChangedListener(object : TextWatcher {
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

        researchCloseButton.setOnClickListener {
            input.setText("")
            mainViewModel.inputCloseButtonClickListener()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        input.removeTextChangedListener(null)
    }
}

