package campus.tech.kakao.map.view.search

import campus.tech.kakao.map.model.SavedLocation

interface OnItemSelectedListener {
    fun onLocationViewClicked(title: String, longitude: String, latitude: String, address:String)
    fun onSavedLocationXButtonClicked(item: SavedLocation)
    fun onSavedLocationViewClicked(title: String)
}