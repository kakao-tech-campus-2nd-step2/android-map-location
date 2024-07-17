package campus.tech.kakao.map.view.search

import campus.tech.kakao.map.model.SavedLocation

interface OnItemSelectedListener {
    fun onLocationViewClicked(title: String)
    fun onSavedLocationXButtonClicked(item: SavedLocation)
    fun onSavedLocationViewClicked(title: String)
}