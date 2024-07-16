package campus.tech.kakao.map.data

import android.content.Context
import android.content.SharedPreferences
import campus.tech.kakao.map.domain.model.PlaceVO
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


object PreferenceHelper {
    private const val PREF_NAME = "kakao_map"
    private const val PREF_KEY_SEARCH_QUERY = "search_query"
    private const val PREF_KEY_LAST_PLACE = "last_place"

    fun defaultPrefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun getSearchHistory(context: Context): List<String> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val jsonSearchHistory = prefs.getString(PREF_KEY_SEARCH_QUERY, null)

        return if (jsonSearchHistory != null) {
            val type = object : TypeToken<List<String>>() {}.type
            Gson().fromJson(jsonSearchHistory, type)
        } else {
            emptyList()
        }
    }

    fun removeSearchQuery(context: Context, query: String) {
        val searchHistory = getSearchHistory(context).toMutableList()
        searchHistory.remove(query)
        val jsonSearchHistory = Gson().toJson(searchHistory)
        defaultPrefs(context).edit().putString(PREF_KEY_SEARCH_QUERY, jsonSearchHistory).apply()
    }

    fun saveSearchQuery(context: Context, query: String) {
        val searchHistory = getSearchHistory(context).toMutableList()
        searchHistory.remove(query)
        searchHistory.add(0, query)
        val jsonSearchHistory = Gson().toJson(searchHistory)

        defaultPrefs(context).edit().putString(PREF_KEY_SEARCH_QUERY, jsonSearchHistory).apply()
    }

    fun saveLastPlace(context: Context, place: PlaceVO) {
        val jsonLastPlace = Gson().toJson(place)
        defaultPrefs(context).edit().putString(PREF_KEY_LAST_PLACE, jsonLastPlace).apply()
    }

    fun getLastPlace(context: Context): PlaceVO? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val jsonLastPlace = prefs.getString(PREF_KEY_LAST_PLACE, null)

        return if (jsonLastPlace != null) {
            Gson().fromJson(jsonLastPlace, PlaceVO::class.java)
        } else {
            null
        }
    }
}