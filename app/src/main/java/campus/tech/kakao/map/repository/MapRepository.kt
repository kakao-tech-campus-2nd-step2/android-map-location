package campus.tech.kakao.map.repository

import android.content.Context
import android.content.SharedPreferences
import campus.tech.kakao.map.model.Constants
import campus.tech.kakao.map.view.MapActivity
import com.kakao.vectormap.LatLng

class SharedPreferenceRepository(private val context : Context) {

    val sharedPreferences : SharedPreferences
    val editor : SharedPreferences.Editor

    init {
        sharedPreferences = context.getSharedPreferences(Constants.Keys.KEY_SHARED, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    fun putPos(latitude : Double, longitude : Double){
        editor.putString(Constants.Keys.KEY_LATITUDE, latitude.toString())
        editor.putString(Constants.Keys.KEY_LONGITUDE, longitude.toString())
        editor.apply()
    }

    fun getPos() : LatLng{
        val latitude = sharedPreferences.getString(Constants.Keys.KEY_LATITUDE,
            MapActivity.LATITUDE
        )?.toDouble() ?: MapActivity.LATITUDE.toDouble()
        val longitude = sharedPreferences.getString(Constants.Keys.KEY_LONGITUDE,
            MapActivity.LONGITUDE
        )?.toDouble() ?: MapActivity.LONGITUDE.toDouble()
        return LatLng.from(latitude, longitude)
    }

}