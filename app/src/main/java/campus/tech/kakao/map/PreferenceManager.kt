package campus.tech.kakao.map

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class PreferenceManager(context: Context) {
    private val prefs: SharedPreferences
            = context.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun deleteString(key: String) {
        prefs.edit().remove(key).apply()
    }

    fun getArrayList(key: String): ArrayList<SearchHistory> {
        val stringPrefs = prefs.getString(key, null)
        return if (stringPrefs != null && stringPrefs != "[]") {
            GsonBuilder().create().fromJson(
                stringPrefs, object : TypeToken<ArrayList<SearchHistory>>() {}.type
            )
        } else {
            ArrayList()
        }
    }

    private fun setArrayList(key: String, list: ArrayList<SearchHistory>) {
        val jsonString = GsonBuilder().create().toJson(list)
        prefs.edit().putString(key, jsonString).apply()
    }

    fun deleteArrayListItem(key: String, index: Int) {
        val list = getArrayList(key)
        if (index >= 0 && index < list.size) {
            list.removeAt(index)
            setArrayList(key, list)
        }
    }

    fun savePreference(key: String, history: SearchHistory, list: ArrayList<SearchHistory>) {
        list.add(0, history)
        setArrayList(key, list)
    }
}