package campus.tech.kakao.map.model.datasource

import android.content.Context

class SharedPreferences(context: Context) {
    private val prefs = context.getSharedPreferences("myPref", Context.MODE_PRIVATE)

    fun getString(key: String, defValue: String): String {
        return prefs.getString(key, defValue).toString()
    }

    fun putString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()

    }
}