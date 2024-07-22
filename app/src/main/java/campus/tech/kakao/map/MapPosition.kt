package campus.tech.kakao.map

import android.content.Context
import campus.tech.kakao.map.dto.MapPositionPreferences

object MapPosition {
	private var mapPosition: MapPositionPreferences? = null

	fun getMapPosition(context: Context): MapPositionPreferences {
		if (mapPosition == null) {
			mapPosition = MapPositionPreferences(context)
		}
		return mapPosition!!
	}
}