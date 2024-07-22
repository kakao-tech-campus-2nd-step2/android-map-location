package campus.tech.kakao.map.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel

class MapViewModel(application: Application): AndroidViewModel(application){
    private val prefs: SharedPreferences =
        application.getSharedPreferences("location_data",Context.MODE_PRIVATE)
    @SuppressLint("CommitPrefEdits")
    fun saveLocation(key: String, data:String){
        prefs.edit().putString(key, data).apply()
    }

    fun getLocation(key: String, data: String?): String {
        return prefs.getString(key, data).toString()
    }
}