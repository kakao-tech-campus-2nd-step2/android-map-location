package campus.tech.kakao.map

import android.content.Context
import android.content.SharedPreferences

class PreferencesRepository(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("search_prefs", Context.MODE_PRIVATE)

    fun getSavedSearches(): List<String> {
        val size = sharedPreferences.getInt("search_size", 0)
        val searches = mutableListOf<String>()
        for (i in 0 until size) {
            val search = sharedPreferences.getString("search_$i", null)
            if (search != null) {
                searches.add(search)
            }
        }
        return searches
    }

    fun saveSearches(searches: List<String>) {
        val editor = sharedPreferences.edit()
        editor.putInt("search_size", searches.size)
        searches.forEachIndexed { index, search ->
            editor.putString("search_$index", search)
        }
        editor.apply()
    }
}
