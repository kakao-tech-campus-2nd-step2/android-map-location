package campus.tech.kakao.map.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel

class KakaoMapViewModel(private val context: Context) : ViewModel() {
    fun saveCoordinates(x: Double, y: Double) {
        val sharedPref = context.getSharedPreferences("Coordinates", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("xCoordinate", x.toString())
            putString("yCoordinate", y.toString())
            apply()
        }
    }

    fun saveToBottomSheet(name: String, Address: String) {
        val sharedPref = context.getSharedPreferences("BottomSheet", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("name", name)
            putString("address", Address)
            apply()
        }
    }
}