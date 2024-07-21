package campus.tech.kakao.map.dto

import android.content.Context

class MapPositionPreferences(context: Context) {

	private val mapPosition = context.getSharedPreferences(MapPositionContract.PREFERENCE_NAME, Context.MODE_PRIVATE)

	fun setPreferences(key: String, value: String) {
		mapPosition.edit().putString(key, value).apply()
	}

	fun getPreferences(key: String, defaultValue: String): String {
		return mapPosition.getString(key, defaultValue).toString()
	}

	fun setMapInfo(document: Document){
		setPreferences(MapPositionContract.PREFERENCE_KEY_LATITUDE, document.latitude)
		setPreferences(MapPositionContract.PREFERENCE_KEY_LONGITUDE, document.longitude)
		setPreferences(MapPositionContract.PREFERENCE_KEY_PLACENAME, document.placeName)
		setPreferences(MapPositionContract.PREFERENCE_KEY_ADDRESSNAME, document.addressName)
	}

}