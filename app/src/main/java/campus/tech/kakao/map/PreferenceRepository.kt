package campus.tech.kakao.map

import android.content.Context
import android.content.SharedPreferences

class PreferenceRepository(context: Context) {

    private var sharedPrefs: SharedPreferences =
        context.getSharedPreferences("location_data", Context.MODE_PRIVATE)

    fun setString(key: String, value: String) {
        sharedPrefs.edit().putString(key, value).apply()
    }

    fun getString(key: String, value: String?): String {
        return sharedPrefs.getString(key, value).toString()
    }
}