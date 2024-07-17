package campus.tech.kakao.map.view

import campus.tech.kakao.map.model.Place
import campus.tech.kakao.map.model.SavedPlace

interface OnClickPlaceListener {
    fun savePlace(place: Place)
}

interface OnClickSavedPlaceListener {
    fun deleteSavedPlace(savedPlace: SavedPlace, position: Int)
}