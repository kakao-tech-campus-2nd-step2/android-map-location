package campus.tech.kakao.map.repository

import android.content.Context
import campus.tech.kakao.map.model.PlaceDBHelper
import campus.tech.kakao.map.model.SavePlace


class SearchRepository(context: Context) {
    private val dbHelper = PlaceDBHelper(context)

    private fun savePlaces(placeName: String) {
        dbHelper.savePlaces(placeName)
    }

    fun showSavePlace(): MutableList<SavePlace> {
        return dbHelper.showSavePlace()
    }

    private fun deleteSavedPlace(savedPlaceName: String) {
        dbHelper.deleteSavedPlace(savedPlaceName)
    }

    fun savePlacesAndUpdate(placeName: String): MutableList<SavePlace> {
        savePlaces(placeName)
        return showSavePlace()
    }

    fun deleteSavedPlacesAndUpdate(savedPlaceName: String): MutableList<SavePlace> {
        deleteSavedPlace(savedPlaceName)
        return showSavePlace()
    }
}