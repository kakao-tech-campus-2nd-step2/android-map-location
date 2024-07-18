package campus.tech.kakao.map.repository

import android.content.Context
import campus.tech.kakao.map.view.ActivityKeys
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.kakao.vectormap.LatLng

class LastPositionRepository(context: Context) {
    private val sharedPreferences = context.getSharedPreferences(
        ActivityKeys.PREFS,
        Context.MODE_PRIVATE
    )

    fun saveLastPosition(position: LatLng) {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(position)
        editor.putString(ActivityKeys.INTENT_PLACE, json)
        editor.apply()
    }

    fun loadLastPosition(): LatLng? {
        if (sharedPreferences.contains(ActivityKeys.PREFS_PLACE)) {
            val gson = Gson()
            val json = sharedPreferences.getString(ActivityKeys.PREFS_PLACE, "")
            try {
                return gson.fromJson(json, LatLng::class.java)
            } catch (e: JsonParseException) {
                e.printStackTrace()
            }
        }
        return null
    }
}