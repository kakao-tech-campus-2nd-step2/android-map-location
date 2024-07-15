package campus.tech.kakao.map.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.model.Document
import campus.tech.kakao.map.model.PlaceResponse
import campus.tech.kakao.map.model.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaceViewModel(private val context: Context) : ViewModel() {

    companion object { private const val API_KEY = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}" }

    private val retrofit = RetrofitInstance.api
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("SavedQueries", Context.MODE_PRIVATE)
    private val _places = MutableLiveData<List<Document>>()
    private val _savedQueries = MutableLiveData<MutableList<String>>()
    val places: LiveData<List<Document>> get() = _places
    val savedQueries: LiveData<MutableList<String>> get() = _savedQueries

    init { _savedQueries.value = loadSavedQueries() }

    fun loadPlaces(query: String, categoryGroupName: String) {
        searchPlaces(query, categoryGroupName) { places ->
            _places.value = places
        }
    }

    fun addSavedQuery(query: String) {
        val updatedList = _savedQueries.value.orEmpty().toMutableList()
        updatedList.add(query)
        _savedQueries.value = updatedList
        saveQueries(updatedList)
    }

    fun removeSavedQuery(query: String) {
        val updatedList = _savedQueries.value.orEmpty().toMutableList()
        updatedList.remove(query)
        _savedQueries.value = updatedList
        saveQueries(updatedList)
    }


    private fun loadSavedQueries(): MutableList<String> {
        val savedQueriesString = sharedPreferences.getString("queries", null)
        return if (!savedQueriesString.isNullOrEmpty()) {
            savedQueriesString.split(",").toMutableList()
        } else {
            mutableListOf()
        }
    }

    private fun saveQueries(queries: MutableList<String>) {
        val editor = sharedPreferences.edit()
        editor.putString("queries", queries.joinToString(","))
        editor.apply()
    }

    private fun searchPlaces(query: String, categoryGroupName: String, callback: (List<Document>) -> Unit) {
        retrofit.getPlaces(API_KEY, categoryGroupName, query).enqueue(object :
            Callback<PlaceResponse> {
            override fun onResponse(call: Call<PlaceResponse>, response: Response<PlaceResponse>) {
                if (response.isSuccessful) {
                    val places = response.body()?.documents ?: emptyList()
                    callback(places)
                } else {
                    Log.e("PlaceViewModel", "Failed to fetch places: ${response.errorBody()?.string()}")
                    callback(emptyList())
                }
            }

            override fun onFailure(call: Call<PlaceResponse>, t: Throwable) {
                Log.e("PlaceViewModel", "Failed to fetch places: ${t.message}")
                callback(emptyList())
            }
        })
    }
}