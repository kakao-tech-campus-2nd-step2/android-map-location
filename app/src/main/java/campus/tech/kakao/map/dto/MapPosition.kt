package campus.tech.kakao.map.dto

import android.content.Context

object MapPosition {
	private var mapPosition: MapPositionPreferences? = null

	fun getMapPosition(context: Context): MapPositionPreferences {
		if (mapPosition == null) {
			mapPosition = MapPositionPreferences(context)
		}
		return mapPosition!!
	}
}