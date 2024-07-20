package campus.tech.kakao.map.repository

import android.content.Context
import campus.tech.kakao.map.base.MyApplication

class MapRepository(private val application: MyApplication) {
    private val sharedPreferences = application.getSharedPreferences("LastLocation", Context.MODE_PRIVATE)

    fun getLastLocation(): Pair<Double, Double>? {
        val x = sharedPreferences.getString("PLACE_X", null)
        val y = sharedPreferences.getString("PLACE_Y", null)
        return if (x != null && y != null) {
            Pair(x.toDouble(), y.toDouble())
        } else {
            null
        }
    }
}