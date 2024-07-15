package campus.tech.kakao.map

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val mapItemViewModel = MapItemViewModel(this)

        val mapList = findViewById<RecyclerView>(R.id.mapList)
        val selectList = findViewById<RecyclerView>(R.id.selectList)
        val inputSpace = findViewById<EditText>(R.id.inputSpace)
        val mainText = findViewById<TextView>(R.id.main_text)
        val cancelBtn = findViewById<ImageView>(R.id.cancelBtn)

        val mapListAdapter = MapListAdapter(listOf(), LayoutInflater.from(this))
        val selectListAdapter = SelectListAdapter(listOf(), LayoutInflater.from(this))

        mapList.adapter = mapListAdapter
        mapList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        selectList.adapter = selectListAdapter
        selectList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        mapListAdapter.setItemClickListener(object : MapListAdapter.ItemClickListener {
            override fun onClick(v: View, mapItem: KakaoMapItem) {
                mapItemViewModel.insertSelectItem(mapItem)
            }
        })

        selectListAdapter.setCancelBtnClickListener(object :
            SelectListAdapter.CancelBtnClickListener {
            override fun onClick(v: View, selectItem: KakaoMapItem) {
                mapItemViewModel.deleteSelectItem(selectItem.id)
            }
        })

        mapItemViewModel.kakaoMapItemList.observe(this) {
            mapListAdapter.updateMapItemList(it)
            if (mapItemViewModel.kakaoMapItemList.value == null) {
                mainText.visibility = View.VISIBLE
            } else if (mapItemViewModel.kakaoMapItemList.value!!.isEmpty()){
                mainText.visibility = View.VISIBLE
            } else {
                mainText.visibility = View.INVISIBLE
            }
        }

        mapItemViewModel.selectItemList.observe(this) {
            selectListAdapter.updateMapItemList(it)
        }

        inputSpace.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                CoroutineScope(Dispatchers.Default).launch {
                    mapItemViewModel.searchKakaoMapItem(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        cancelBtn.setOnClickListener {
            inputSpace.setText("")
        }
    }
}


