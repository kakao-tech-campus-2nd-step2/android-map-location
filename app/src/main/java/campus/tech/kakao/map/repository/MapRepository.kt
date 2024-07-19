package campus.tech.kakao.map.repository

import android.content.Context
import android.content.SharedPreferences

class MapRepository(context: Context) {
    private val spf: SharedPreferences =
        context.getSharedPreferences("map_prefs", Context.MODE_PRIVATE)

    fun saveLastPosition(latitude: Double, longitude: Double) {
        val editor = spf.edit()
        editor.putString("latitude", latitude.toString())
        editor.putString("longitude", longitude.toString())
        editor.apply()
    }

    fun getLastPosition(): Pair<Double, Double> {
        val latitude = spf.getString("latitude", "37.394660")?.toDouble() ?: 37.394660
        val longitude = spf.getString("longitude", "127.111182")?.toDouble() ?: 127.111182
        return Pair(latitude, longitude)
    }
}
