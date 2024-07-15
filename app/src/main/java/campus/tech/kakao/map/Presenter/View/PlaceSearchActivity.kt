package campus.tech.kakao.map.Presenter.View

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout.VERTICAL
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.Data.Datasource.Local.SqliteDB
import campus.tech.kakao.map.R
import campus.tech.kakao.map.Base.ViewModelFactory
import campus.tech.kakao.map.Domain.Model.Place
import campus.tech.kakao.map.MyApplication
import campus.tech.kakao.map.Presenter.View.Adapter.FavoriteAdapter
import campus.tech.kakao.map.Presenter.View.Adapter.SearchResultAdapter
import campus.tech.kakao.map.Presenter.View.Observer.EmptyPlaceObserver
import campus.tech.kakao.map.ViewModel.SearchViewModel

class PlaceSearchActivity : AppCompatActivity() {
    private lateinit var viewModel: SearchViewModel
    private lateinit var searchResult: RecyclerView
    private lateinit var noItem: TextView
    private lateinit var etSearchPlace: EditText
    private lateinit var deleteSearch: ImageView
    private lateinit var favorite: RecyclerView
    private lateinit var sqliteDB: SqliteDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_search)

        val repository = (application as MyApplication).appContainer.repository

        viewModel =
            ViewModelProvider(this, ViewModelFactory(repository))[SearchViewModel::class.java]

        searchResult = findViewById<RecyclerView>(R.id.searchResult)
        etSearchPlace = findViewById<EditText>(R.id.etSearchPlace)
        noItem = findViewById<TextView>(R.id.noItem)
        deleteSearch = findViewById<ImageView>(R.id.deleteSearch)
        favorite = findViewById<RecyclerView>(R.id.favorite)




        settingSearchRecyclerView()
        settingFavoriteRecyclerView()
        setDeleteSearchListener()
        setEditTextListener()

    }

    private fun settingSearchRecyclerView() {
        setSearchAdapter()
        searchResult.layoutManager = LinearLayoutManager(this)
        searchResult.addItemDecoration(
            DividerItemDecoration(this, VERTICAL)
        )
    }

    private fun settingFavoriteRecyclerView() {
        setFavoriteAdapter()
        favorite.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true)
    }

    private fun setFavoriteAdapter() {
        val adapter = FavoriteAdapter(
            onClickDelete = {
                viewModel.deleteFromFavorite(it)
            })

        favorite.adapter = adapter
        viewModel.favoritePlace.observe(this) {
            adapter.submitList(it)
            favorite.smoothScrollToPosition(maxOf(it.size-1,0))
        }
    }


    private fun setDeleteSearchListener() {
        deleteSearch.setOnClickListener {
            etSearchPlace.setText("")
        }
    }

    private fun setEditTextListener() {
        etSearchPlace.addTextChangedListener {
            Thread {
                viewModel.searchPlaceRemote(etSearchPlace.text.toString())
            }.start()
        }
    }

    private fun setSearchAdapter() {
        val adapter = SearchResultAdapter(
            onClickAdd = {
                viewModel.addFavorite(it)
            })
        viewModel.currentResult.observe(this) {
            adapter.submitList(it)
            handleVisibility(it)
        }
        searchResult.adapter = adapter
    }

    private fun handleVisibility(places : List<Place>){
        if(places.isEmpty()){
            searchResult.visibility = GONE
            noItem.visibility = VISIBLE
        } else {
            searchResult.visibility = VISIBLE
            noItem.visibility = GONE
        }
    }

}




















